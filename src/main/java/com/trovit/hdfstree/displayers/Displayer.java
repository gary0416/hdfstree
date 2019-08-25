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
package com.trovit.hdfstree.displayers;

import com.trovit.hdfstree.TreeNode;

import java.util.concurrent.atomic.LongAdder;

public interface Displayer {

    // display the tree on screen.
    void display(TreeNode tree, LongAdder dirCounter, LongAdder fileCounter);

    // display size as well.
    void setDisplaySize();

    void setMaxDepth(int d);

    void setDisplayFiles();
}