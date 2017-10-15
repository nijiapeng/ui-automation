/*
 * Copyright 2017 inpwtepydjuf@gmail.com
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
import mmarquee.automation.controls.AutomationWindow;
import mmarquee.automation.controls.menu.AutomationMenu;
import mmarquee.automation.pattern.PatternNotFoundException;
import mmarquee.automation.uiautomation.*;

/**
 * The legacy base automation wrapper.
 *
 * This implements the interfaces, etc that are only available in Windows 8 and before.
 *
 * @author Mark Humphreys
 * Date 15/10/2017.
 *
 */
public class UIAutomationLegacy extends UIAutomationBase {

    /**
     * The legacy automation instance.
     */
    protected static UIAutomationLegacy INSTANCE = null;

    /**
     * Created for test, to allow mocking.
     *
     * @param automation The automation object to use.
     */
    public UIAutomationLegacy(final IUIAutomation automation) {
        this.automation = automation;
    }

    /**
     * Constructor for UIAutomation library.
     */
    protected UIAutomationLegacy() {
        super();

        PointerByReference pRoot = new PointerByReference();

        this.getRootElement(pRoot);

        Unknown uRoot = new Unknown(pRoot.getValue());

        WinNT.HRESULT result0 = uRoot.QueryInterface(new Guid.REFIID(IUIAutomationElement2.IID), pRoot);

        if (COMUtils.SUCCEEDED(result0)) {
            this.rootElement = new AutomationElement(IUIAutomationElement3Converter.PointerToInterface(pRoot));
        }
    }

    /**
     * Gets the instance.
     *
     * @return the instance of the ui automation library.
     */
    public static UIAutomationLegacy getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UIAutomationLegacy();
        }

        return INSTANCE;
    }

    /**
     * Gets the desktop 'window' associated with the title.
     *
     * @param title Title to search for.
     * @return AutomationWindow The found window.
     * @throws ElementNotFoundException Element is not found.
     * @throws PatternNotFoundException Expected pattern not found.
     */
    public AutomationWindow getDesktopWindow(final String title)
            throws PatternNotFoundException, AutomationException {
        return new AutomationWindow(this.get(ControlType.Window, title, FIND_DESKTOP_ATTEMPTS));
    }

    /**
     * Gets the desktop 'window' associated with the title, with a variable
     * number of retries.
     *
     * @param title Title to search for.
     * @param retries Number of retries.
     * @return AutomationWindow The found window.
     * @throws ElementNotFoundException Element is not found.
     * @throws PatternNotFoundException Expected pattern not found.
     */
    public AutomationWindow getDesktopWindow(final String title, final int retries)
            throws PatternNotFoundException, AutomationException {
        return new AutomationWindow(this.get(ControlType.Window, title, retries));
    }

    /**
     * Creates a condition, based on control id.
     *
     * @param id The control id.
     * @return The condition.
     * @throws AutomationException Something went wrong.
     */
    public PointerByReference createControlTypeCondition(final ControlType id)
            throws AutomationException {
        Variant.VARIANT.ByValue variant = new Variant.VARIANT.ByValue();
        variant.setValue(Variant.VT_INT, id.getValue());

        return this.createPropertyCondition(PropertyID.ControlType.getValue(), variant);
    }

    /**
     * Creates a condition, based on automation id.
     *
     * @param automationId The automation id.
     * @return The condition.
     * @throws AutomationException Something went wrong.
     */
    public PointerByReference createAutomationIdPropertyCondition(final String automationId)
            throws AutomationException {
        Variant.VARIANT.ByValue variant = new Variant.VARIANT.ByValue();
        WTypes.BSTR sysAllocated = OleAuto.INSTANCE.SysAllocString(automationId);
        variant.setValue(Variant.VT_BSTR, sysAllocated);

        try {
            return this.createPropertyCondition(PropertyID.AutomationId.getValue(), variant);
        } finally {
            OleAuto.INSTANCE.SysFreeString(sysAllocated);
        }
    }

    /**
     * Creates a condition, based on element name.
     *
     * @param name The name.
     * @return The condition.
     * @throws AutomationException Something went wrong.
     */
    public PointerByReference createNamePropertyCondition(final String name)
            throws AutomationException {
        Variant.VARIANT.ByValue variant = new Variant.VARIANT.ByValue();
        WTypes.BSTR sysAllocated = OleAuto.INSTANCE.SysAllocString(name);
        variant.setValue(Variant.VT_BSTR, sysAllocated);

        try {
            return this.createPropertyCondition(PropertyID.Name.getValue(), variant);
        } finally {
            OleAuto.INSTANCE.SysFreeString(sysAllocated);
        }
    }

    /**
     * Gets the desktop object associated with the title.
     *
     * @param title Title of the menu to search for.
     * @return AutomationMenu The found menu.
     * @throws ElementNotFoundException Element is not found.
     */
    public AutomationMenu getDesktopMenu(final String title)
            throws AutomationException {
        AutomationElement element = null;

        // Look for a specific title
        Variant.VARIANT.ByValue variant = new Variant.VARIANT.ByValue();
        WTypes.BSTR sysAllocated = OleAuto.INSTANCE.SysAllocString(title);
        variant.setValue(Variant.VT_BSTR, sysAllocated);

        try {
            PointerByReference pCondition1 = this.createPropertyCondition(PropertyID.Name.getValue(), variant);

            for (int loop = 0; loop < FIND_DESKTOP_ATTEMPTS; loop++) {

                try {
                    element = this.rootElement.findFirst(new TreeScope(TreeScope.Descendants),
                            pCondition1);
                } catch (AutomationException ex) {
                    logger.info("Not found, retrying " + title);
                }

                if (element != null) {
                    break;
                }
            }
        } finally {
            OleAuto.INSTANCE.SysFreeString(sysAllocated);
        }

        if (element == null) {
            logger.info("Failed to find desktop menu `" + title + "`");
            throw new ItemNotFoundException(title);
        }

        return new AutomationMenu(element);
    }

    /**
     * Gets the element from the supplied tag.
     * @param pt The point.
     * @return The actual element under the tag.
     * @throws AutomationException The automation library returned an error
     */
    public AutomationElementLegacy getElementFromPoint(final WinDef.POINT pt)
            throws AutomationException {
        PointerByReference pbr = new PointerByReference();

        final int res = this.automation.elementFromPoint(pt, pbr);
        if (res == 0) {
            IUIAutomationElement2 element = getAutomationElementFromReferenceLegacy(pbr);

            return new AutomationElementLegacy(element);
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
    public AutomationElementLegacy getElementFromHandle(final WinDef.HWND hwnd)
            throws AutomationException {
        PointerByReference pbr = new PointerByReference();

        final int res = this.automation.getElementFromHandle(hwnd, pbr);
        if (res == 0) {
            IUIAutomationElement2 element = getAutomationElementFromReferenceLegacy(pbr);

            return new AutomationElementLegacy(element);
        } else {
            throw new AutomationException(res);
        }
    }

    /**
     * Gets the focused element.
     * @return The focused element.
     * @throws AutomationException Automation returned an error.
     */
    public AutomationElementLegacy getFocusedElement()
            throws AutomationException {
        PointerByReference pbr = new PointerByReference();

        final int res = this.automation.getFocusedElement(pbr);
        if (res == 0) {
            IUIAutomationElement2 element = getAutomationElementFromReferenceLegacy(pbr);

            return new AutomationElementLegacy(element);
        } else {
            throw new AutomationException(res);
        }
    }
}
