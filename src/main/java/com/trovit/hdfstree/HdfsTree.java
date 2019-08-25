/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.trovit.hdfstree;

import com.trovit.hdfstree.displayers.ConsoleDisplayer;
import com.trovit.hdfstree.displayers.Displayer;
import com.trovit.hdfstree.fsinspectors.FSInspector;
import com.trovit.hdfstree.fsinspectors.HDFSInspector;
import com.trovit.hdfstree.fsinspectors.LocalFSInspector;
import org.apache.commons.cli.*;

import java.util.concurrent.atomic.LongAdder;

public class HdfsTree {

    public static void main(String... args) {
        Options options = new Options();
        options.addOption("l", false, "Use local filesystem.");
        options.addOption("s", false, "Display the size of the directory");
        options.addOption("d", false, "Maximum depth of the tree (when displaying)");
        options.addOption("a", false, "Output directories and files");

        CommandLineParser parser = new PosixParser();

        TreeBuilder treeBuilder;
        FSInspector fsInspector = null;
        String rootPath = null;

        Displayer displayer = new ConsoleDisplayer();

        try {
            CommandLine cmd = parser.parse(options, args);

            // local or hdfs.
            if (cmd.hasOption("l")) {
                fsInspector = new LocalFSInspector();
            } else {
                fsInspector = new HDFSInspector();
            }

            String[] otherArgs = cmd.getArgs();
            if (otherArgs.length == 0) {
                throw new ParseException("path is not specified.");
            } else {
                rootPath = otherArgs[0];
            }

            if (cmd.hasOption("d")) {
                displayer.setMaxDepth(Integer.parseInt(cmd.getOptionValue("d")));
            } else {
                displayer.setMaxDepth(100);
            }

            if (cmd.hasOption("s")) {
                displayer.setDisplaySize();
            }

            if (cmd.hasOption("a")) {
                displayer.setDisplayFiles();
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("hdfstree [path]", options);
            System.exit(1);
        }

        treeBuilder = new TreeBuilder(rootPath, fsInspector);
        TreeNode tree = treeBuilder.buildTree();

        LongAdder dirCounter = new LongAdder();
        LongAdder fileCounter = new LongAdder();
        displayer.display(tree, dirCounter, fileCounter);
    }
}