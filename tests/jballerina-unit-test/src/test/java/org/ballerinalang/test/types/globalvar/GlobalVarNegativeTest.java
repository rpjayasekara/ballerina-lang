/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
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

package org.ballerinalang.test.types.globalvar;

import org.ballerinalang.test.BAssertUtil;
import org.ballerinalang.test.BCompileUtil;
import org.ballerinalang.test.CompileResult;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Global variable error scenarios.
 */
public class GlobalVarNegativeTest {

    @Test(groups = { "disableOnOldParser" })
    public void testGlobalVarNegatives() {
        CompileResult resultNegative = BCompileUtil.compile(
                "test-src/statements/variabledef/global_variable_negative.bal");
        Assert.assertEquals(resultNegative.getErrorCount(), 7);
        int i = 0;
        BAssertUtil.validateError(resultNegative, i++, "missing non-defaultable required record field 'x'", 22, 12);
        BAssertUtil.validateError(resultNegative, i++, "invalid character ':' in field access expression", 23, 15);
        BAssertUtil.validateError(resultNegative, i++, "missing equal token", 27, 46);
        BAssertUtil.validateError(resultNegative, i++, "missing identifier", 27, 46);
        BAssertUtil.validateError(resultNegative, i++, "missing equal token", 29, 59);
        BAssertUtil.validateError(resultNegative, i++, "missing identifier", 29, 59);
        BAssertUtil.validateError(resultNegative, i++, "invalid cyclic type reference in '[Listener, Listener]'", 31,
                1);
    }

    @Test
    void testGlobalVariableInitNegative() {
        CompileResult result = BCompileUtil.compile("test-src/statements/variabledef/global_variable_init_negative" +
                ".bal");

        Assert.assertEquals(result.getErrorCount(), 8);
        int i = 0;
        BAssertUtil.validateError(result, i++, "uninitialized variable 'i'", 17, 1);
        BAssertUtil.validateError(result, i++, "uninitialized variable 's'", 18, 1);
        BAssertUtil.validateError(result, i++, "uninitialized variable 'a'", 19, 1);
        BAssertUtil.validateError(result, i++, "uninitialized variable 'b'", 20, 1);
        BAssertUtil.validateError(result, i++, "variable 'i' is not initialized", 25, 5);
        BAssertUtil.validateError(result, i++, "variable 'i' is not initialized", 31, 5);
        BAssertUtil.validateError(result, i++, "variable 'a' is not initialized", 39, 13);
        BAssertUtil.validateError(result, i, "variable 's' is not initialized", 40, 18);
    }

    @Test
    void testGlobalVariableInitWithInvocationNegative() {
        CompileResult result = BCompileUtil.compile("test-src/statements/variabledef" +
                "/global_variable_init_with_invocation_negative.bal");

        Assert.assertEquals(result.getErrorCount(), 2);
        int i = 0;
        BAssertUtil.validateError(result, i++, "variable(s) 'i, s' not initialized", 21, 9);
        BAssertUtil.validateError(result, i, "variable(s) 's' not initialized", 22, 9);
    }

    @Test
    public void testConfigurableModuleVarDeclNegative() {
        CompileResult result = BCompileUtil.compile
                ("test-src/statements/variabledef/configurable_global_var_decl_negative.bal");
        int i = 0;
        BAssertUtil.validateError(result, i++, "configurable variable must be initialized or be marked as required",
                18, 19);
        BAssertUtil.validateError(result, i++, "configurable variable cannot be declared with var", 20, 1);
        BAssertUtil.validateError(result, i++, "invalid type for configurable variable: expected a subtype" +
                " of 'anydata'", 22, 22);
        BAssertUtil.validateError(result, i++, "missing close brace token", 27, 1);
        BAssertUtil.validateError(result, i++, "configurable variable currently not supported for " +
                "'(json & readonly)'", 31, 1);
        BAssertUtil.validateError(result, i++, "invalid token '}'", 31, 1);
        BAssertUtil.validateError(result, i++, "only simple variables are allowed to be configurable", 34, 1);
        BAssertUtil.validateError(result, i++, "'final' qualifier not allowed: configurable variables are " +
                "implicitly final", 37, 7);
        BAssertUtil.validateError(result, i++, "configurable variable currently not supported for '(table<Person> key" +
                "(name)[] & readonly)'\n\t" +
                "array element type 'table<Person> key(name) & readonly' is not supported", 61, 1);
        BAssertUtil.validateError(result, i++, "configurable variable currently not supported for '((table<Person> " +
                        "key(name) & readonly)[] & readonly)'\n\t" +
                "array element type 'table<Person> key(name) & readonly' is not supported", 62, 1);
        BAssertUtil.validateError(result, i++, "configurable variable currently not supported for '(Person1" +
                " & readonly)'\n\t" +
                "record field type '(anydata & readonly)' of field 'anydataField' is not supported", 65, 1);
        BAssertUtil.validateError(result, i++, "configurable variable currently not supported for '(Person2 & " +
                "readonly)'\n\t" +
                "record field type '(int|string)' of field 'unionField' is not supported\n\t" +
                "record field type '(anydata & readonly)' of field 'anydataField' is not supported", 66, 1);
        BAssertUtil.validateError(result, i++, "configurable variable currently not supported for '(Person3 & " +
                "readonly)'\n\t" +
                "array element type '1|2' is not supported", 67, 1);
        BAssertUtil.validateError(result, i++, "configurable variable currently not supported for '" +
                "(table<map<anydata>> & readonly)'\n\t" +
                "map constraint type '(anydata & readonly)' is not supported", 70, 1);
        BAssertUtil.validateError(result, i++, "configurable variable currently not supported for '(table<Person1> & " +
                "readonly)'\n\t" +
                "record field type '(anydata & readonly)' of field 'anydataField' is not supported", 71, 1);
        BAssertUtil.validateError(result, i++, "configurable variable currently not supported for '(anydata[] & " +
                "readonly)'\n\t" +
                "array element type 'anydata & readonly' is not supported", 74, 1);
        BAssertUtil.validateError(result, i++, "configurable variable currently not supported for '(map<anydata> & " +
                "readonly)'\n\t" +
                "map constraint type '(anydata & readonly)' is not supported", 77, 1);
        BAssertUtil.validateError(result, i++, "configurable variable currently not supported for '((string|int) & " +
                "readonly)'", 80, 1);
        BAssertUtil.validateError(result, i++, "configurable variable currently not supported for '(anydata & " +
                "readonly)'", 81, 1);
        BAssertUtil.validateError(result, i++, "configurable variable currently not supported for 'Red|Green'", 84, 1);
        BAssertUtil.validateError(result, i++, "configurable variable currently not supported for '1|0'", 85, 1);
        BAssertUtil.validateError(result, i++, "configurable variable currently not supported for '2|1.7f|test'", 86,
                1);
        BAssertUtil.validateError(result, i++, "configurable variable currently not supported for '([int,string] & " +
                        "readonly)'", 89, 1);
        Assert.assertEquals(result.getErrorCount(), i);
    }

    @Test
    public void testConfigurableImplicitFinal() {
        CompileResult result = BCompileUtil.compile
                ("test-src/statements/variabledef/configurable_global_var_decl_negative_02.bal");
        int i = 0;
        BAssertUtil.validateError(result, i++, "cannot assign a value to final 'discountRate'",
                21, 5);
        Assert.assertEquals(result.getErrorCount(), i);
    }

    @Test
    public void testConfigurableImplicitReadOnly() {
        CompileResult result = BCompileUtil.compile
                ("test-src/statements/variabledef/configurable_global_var_decl_negative_03.bal");
        int i = 0;
        BAssertUtil.validateError(result, i++, "incompatible types: expected 'int[] & readonly', found 'int[]'",
                20, 27);
        BAssertUtil.validateError(result, i++, "incompatible types: expected '(Foo & readonly)', found 'Foo'",
                28, 22);
        Assert.assertEquals(result.getErrorCount(), i);
    }
}
