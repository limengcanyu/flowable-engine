/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flowable.engine.impl.bpmn.helper;

import java.util.List;

import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.common.engine.api.variable.VariableContainer;
import org.flowable.engine.impl.bpmn.parser.FieldDeclaration;
import org.flowable.engine.impl.cfg.DelegateExpressionFieldInjectionMode;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.variable.api.delegate.VariableScope;

/**
 * @author Joram Barrez
 */
public class DelegateExpressionUtil {

    public static Object resolveDelegateExpression(Expression expression, VariableScope variableScope) {
        return resolveDelegateExpression(expression, variableScope, null);
    }

    public static Object resolveDelegateExpression(Expression expression,
                                                   VariableContainer variableScope, List<FieldDeclaration> fieldDeclarations) {

        // Note: we can't cache the result of the expression, because the
        // execution can change: eg. delegateExpression='${mySpringBeanFactory.randomSpringBean()}'
        Object delegate = expression.getValue(variableScope);

        if (fieldDeclarations != null && fieldDeclarations.size() > 0) {

            DelegateExpressionFieldInjectionMode injectionMode = CommandContextUtil.getProcessEngineConfiguration().getDelegateExpressionFieldInjectionMode();
            if (injectionMode == DelegateExpressionFieldInjectionMode.COMPATIBILITY) {
                ClassDelegate.applyFieldDeclaration(fieldDeclarations, delegate, true);
            } else if (injectionMode == DelegateExpressionFieldInjectionMode.MIXED) {
                ClassDelegate.applyFieldDeclaration(fieldDeclarations, delegate, false);
            }

        }

        return delegate;
    }

}
