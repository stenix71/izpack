/*
 * IzPack - Copyright 2001-2008 Julien Ponge, All Rights Reserved.
 *
 * http://izpack.org/
 * http://izpack.codehaus.org/
 *
 * Copyright 2007-2009 Dennis Reil
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.izforge.izpack.core.rules.process;

import com.izforge.izpack.api.adaptator.IXMLElement;
import com.izforge.izpack.api.adaptator.impl.XMLElementImpl;
import com.izforge.izpack.api.rules.Condition;
import com.izforge.izpack.util.Debug;

/**
 * @author Dennis Reil, <izpack@reil-online.de>
 */
public class CompareNumericsCondition extends Condition
{
    private static final long serialVersionUID = 5631805710151645907L;

    private String variablename;
    private String value;
    private String operator;

    /**
     * {@inheritDoc}
     */
    @Override
    public void readFromXML(IXMLElement xmlcondition)
    {
        try
        {
            this.variablename = xmlcondition.getFirstChildNamed("name").getContent();
            this.value = xmlcondition.getFirstChildNamed("value").getContent();
            this.operator = xmlcondition.getFirstChildNamed("operator").getContent();
        }
        catch (Exception e)
        {
            Debug.log("missing element in <condition type=\"variable\"/>");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTrue()
    {
        boolean result = false;
        if (this.getInstalldata() != null)
        {
            String val = this.getInstalldata().getVariable(variablename);
            if (val != null)
            {
                if (operator == null)
                {
                    operator = "eq";
                }
                try
                {
                    int currentValue = new Integer(val);
                    int comparisonValue = new Integer(value);
                    if ("eq".equalsIgnoreCase(operator))
                    {
                        result = currentValue == comparisonValue;
                    }
                    else if ("gt".equalsIgnoreCase(operator))
                    {
                        result = currentValue > comparisonValue;
                    }
                    else if ("lt".equalsIgnoreCase(operator))
                    {
                        result = currentValue < comparisonValue;
                    }
                    else if ("leq".equalsIgnoreCase(operator))
                    {
                        result = currentValue <= comparisonValue;
                    }
                    else if ("geq".equalsIgnoreCase(operator))
                    {
                        result = currentValue >= comparisonValue;
                    }
                }
                catch (NumberFormatException nfe)
                {
                    Debug.log("The value of the associated variable is not a numeric value or the value which should be compared is not a number.");
                }
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDependenciesDetails()
    {
        StringBuffer details = new StringBuffer();
        details.append(this.getId());
        details.append(" depends on a value of <b>");
        details.append(this.value);
        details.append("</b> on variable <b>");
        details.append(this.variablename);
        details.append(" (current value: ");
        details.append(this.getInstalldata().getVariable(variablename));
        details.append(")");
        details.append("This value has to be " + this.operator);
        details.append("</b><br/>");
        return details.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void makeXMLData(IXMLElement conditionRoot)
    {
        XMLElementImpl nameXml = new XMLElementImpl("name", conditionRoot);
        nameXml.setContent(this.variablename);
        conditionRoot.addChild(nameXml);
        XMLElementImpl valueXml = new XMLElementImpl("value", conditionRoot);
        valueXml.setContent(this.value);
        conditionRoot.addChild(valueXml);
        XMLElementImpl opXml = new XMLElementImpl("op", conditionRoot);
        opXml.setContent(this.operator);
        conditionRoot.addChild(opXml);
    }
}