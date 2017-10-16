/*
 * Copyright 2016-17 inpwtepydjuf@gmail.com
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
package mmarquee.automation;

import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.*;
import com.sun.jna.ptr.PointerByReference;
import mmarquee.automation.uiautomation.*;

/**
 * @author Mark Humphreys
 * Date 26/01/2016.
 *
 * The base automation wrapper.
 */
public class UIAutomation extends UIAutomationBase {

    /**
     * The automation instance.
     */
    protected static UIAutomation INSTANCE = null;

    /**
     * Created for test, to allow mocking.
     *
     * @param automation The automation object to use.
     */
    public UIAutomation(final IUIAutomation automation) {
        super(automation);
    }

    /**
     * Constructor for UIAutomation library.
     */
    protected UIAutomation() {
        super();

        PointerByReference pRoot = new PointerByReference();

        this.getRootElement(pRoot);

        Unknown uRoot = new Unknown(pRoot.getValue());

        WinNT.HRESULT result0 = uRoot.QueryInterface(new Guid.REFIID(IUIAutomationElement.IID), pRoot);

        if (COMUtils.SUCCEEDED(result0)) {
            this.rootElement = new AutomationElement(IUIAutomationElementConverter.PointerToInterface(pRoot));
        }
    }

    /**
     * Gets the instance.
     *
     * @return the instance of the ui automation library.
     */
    public static UIAutomation getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UIAutomation();
        }

        return INSTANCE;
    }

    /**
     * Gets the element from the supplied tag.
     * @param pt The point.
     * @return The actual element under the tag.
     * @throws AutomationException The automation library returned an error
     */
    public AutomationElement getElementFromPoint(final WinDef.POINT pt)
            throws AutomationException {
        PointerByReference pbr = new PointerByReference();

        final int res = this.automation.elementFromPoint(pt, pbr);
        if (res == 0) {
            IUIAutomationElement element = getAutomationElementFromReference(pbr);

            return new AutomationElement(element);
        } else {
            throw new AutomationException(res);
        }
    }

    /**
     * Gets the element from the native handle.
     *
     * @param hwnd Native Handle.
     * @return The actual element under the handle.
     * @throws AutomationException The automation library returned an error.
     */
    public AutomationElement getElementFromHandle(final WinDef.HWND hwnd)
            throws AutomationException {
        PointerByReference pbr = new PointerByReference();

        final int res = this.automation.getElementFromHandle(hwnd, pbr);
        if (res == 0) {
            IUIAutomationElement element = getAutomationElementFromReference(pbr);

            return new AutomationElement(element);
        } else {
            throw new AutomationException(res);
        }
    }

    /**
     * Gets the focused element.
     * @return The focused element.
     * @throws AutomationException Automation returned an error.
     */
    public AutomationElement getFocusedElement()
            throws AutomationException {
        PointerByReference pbr = new PointerByReference();

        final int res = this.automation.getFocusedElement(pbr);
        if (res == 0) {
            IUIAutomationElement element = getAutomationElementFromReference(pbr);

            return new AutomationElement(element);
        } else {
            throw new AutomationException(res);
        }
    }
}
