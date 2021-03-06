/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.netbeans.jellytools.nodes;

import java.awt.Toolkit;
import java.io.IOException;
import junit.framework.Test;
import org.netbeans.jellytools.EditorOperator;
import org.netbeans.jellytools.JellyTestCase;
import org.netbeans.jellytools.SaveAsTemplateOperator;
import org.netbeans.jellytools.testutils.NodeUtils;

/** Test of org.netbeans.jellytools.nodes.HTMLNode
 *
 * @author Adam Sotona
 * @author Jiri Skrivanek
 */
public class HTMLNodeTest extends JellyTestCase {

    public static final String[] tests = {
        "testVerifyPopup",
        "testOpen",
        "testCut",
        "testCopy",
        "testDelete",
        "testRename",
        "testSaveAsTemplate",
        "testProperties"
    };

    /** constructor required by JUnit
     * @param testName method name to be used as testcase
     */
    public HTMLNodeTest(String testName) {
        super(testName);
    }

    /** method used for explicit testsuite definition
     */
    public static Test suite() {
        return createModuleTest(HTMLNodeTest.class, tests);
    }
    protected static HTMLNode htmlNode = null;

    /** Find node. */
    @Override
    protected void setUp() throws IOException {
        System.out.println("### " + getName() + " ###");
        openDataProjects("SampleProject");
        if (htmlNode == null) {
            htmlNode = new HTMLNode(new SourcePackagesNode("SampleProject"),
                    "sample1|html.html"); // NOI18N
        }
    }

    /** Test verifyPopup  */
    public void testVerifyPopup() {
        htmlNode.verifyPopup();
    }

    /** Test open */
    public void testOpen() {
        htmlNode.open();
        new EditorOperator("html").closeDiscard();  // NOI18N
    }

    /** Test cut */
    public void testCut() {
        Object clipboard1 = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        htmlNode.cut();
        NodeUtils.testClipboard(clipboard1);
    }

    /** Test copy */
    public void testCopy() {
        Object clipboard1 = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        htmlNode.copy();
        NodeUtils.testClipboard(clipboard1);
    }

    /** Test delete */
    public void testDelete() {
        htmlNode.delete();
        NodeUtils.closeConfirmDeleteDialog();
    }

    /** Test rename */
    public void testRename() {
        htmlNode.rename();
        NodeUtils.closeRenameDialog();
    }

    /** Test properties */
    public void testProperties() {
        htmlNode.properties();
        NodeUtils.closeProperties("html"); // NOI18N
    }

    /** Test saveAsTemplate */
    public void testSaveAsTemplate() {
        htmlNode.saveAsTemplate();
        new SaveAsTemplateOperator().close();
    }
}
