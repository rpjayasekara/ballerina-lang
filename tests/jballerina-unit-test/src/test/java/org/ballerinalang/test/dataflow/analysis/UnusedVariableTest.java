/*
 *  Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.ballerinalang.test.dataflow.analysis;

import org.ballerinalang.test.BCompileUtil;
import org.ballerinalang.test.CompileResult;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.ballerinalang.test.BAssertUtil.validateWarning;

/**
 * Test cases related to unused variable analysis.
 *
 * @since 2.0.0
 */
@Test
public class UnusedVariableTest {

    @Test
    public void testUnusedVariableAnalysis() {
        CompileResult result = BCompileUtil.compileWithoutInitInvocation(
                "test-src/dataflow/analysis/unused_variable_analysis_test.bal");
        int i = 0;
        validateWarning(result, i++, getUnusedVariableWarning("i"), 18, 5);
        validateWarning(result, i++, getUnusedVariableWarning("j"), 20, 5);
        validateWarning(result, i++, getUnusedVariableWarning("k"), 22, 5);
        validateWarning(result, i++, getUnusedVariableWarning("arr"), 29, 5);
        validateWarning(result, i++, getUnusedVariableWarning("o"), 37, 5);
        validateWarning(result, i++, getUnusedVariableWarning("i"), 43, 5);
        validateWarning(result, i++, getUnusedVariableWarning("a"), 51, 5);
        validateWarning(result, i++, getUnusedVariableWarning("a"), 55, 13);
        validateWarning(result, i++, getUnusedVariableWarning("b"), 55, 16);
        validateWarning(result, i++, getUnusedVariableWarning("c"), 55, 19);
        validateWarning(result, i++, getUnusedVariableWarning("d"), 57, 57);
        validateWarning(result, i++, getUnusedVariableWarning("e"), 57, 61);
        validateWarning(result, i++, getUnusedVariableWarning("f"), 57, 64);
        validateWarning(result, i++, getUnusedVariableWarning("g"), 57, 71);
        validateWarning(result, i++, getUnusedVariableWarning("h"), 61, 64);
        validateWarning(result, i++, getUnusedVariableWarning("m"), 61, 73);
        validateWarning(result, i++, getUnusedVariableWarning("i"), 61, 80);
        validateWarning(result, i++, getUnusedVariableWarning("k"), 61, 83);
        validateWarning(result, i++, getUnusedVariableWarning("l"), 61, 90);
        validateWarning(result, i++, getUnusedVariableWarning("c"), 71, 19);
        validateWarning(result, i++, getUnusedVariableWarning("f"), 74, 64);
        validateWarning(result, i++, getUnusedVariableWarning("h"), 78, 64);
        validateWarning(result, i++, getUnusedVariableWarning("m"), 78, 73);
        validateWarning(result, i++, getUnusedVariableWarning("k"), 78, 83);
        validateWarning(result, i++, getUnusedVariableWarning("l"), 87, 5);
        validateWarning(result, i++, getUnusedVariableWarning("m"), 90, 9);
        validateWarning(result, i++, getUnusedVariableWarning("p"), 96, 9);
        validateWarning(result, i++, getUnusedVariableWarning("a"), 112, 5);
        validateWarning(result, i++, getUnusedVariableWarning("e"), 122, 7);
        validateWarning(result, i++, getUnusedVariableWarning("arr"), 152, 5);
        validateWarning(result, i++, getUnusedVariableWarning("o"), 157, 5);
        validateWarning(result, i++, getUnusedVariableWarning("i"), 163, 5);
        validateWarning(result, i++, getUnusedVariableWarning("a"), 171, 5);
        Assert.assertEquals(result.getWarnCount(), i);
        Assert.assertEquals(result.getErrorCount(), 0);
    }

    private String getUnusedVariableWarning(String varName) {
        return "unused variable '" + varName + "'";
    }
}
