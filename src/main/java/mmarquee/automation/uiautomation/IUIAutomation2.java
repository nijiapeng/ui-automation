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
package mmarquee.automation.uiautomation;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.COM.IUnknown;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * Automation interface for Windows 8 and above.
 *
 * @author Mark Humphreys
 * Date 12/09/2016.
 *
 */
public interface IUIAutomation2 extends IUIAutomation {
    /**
     * The interface IID for QueryInterface et al
     */
    Guid.IID IID = new Guid.IID("{34723AFF-0C9D-49D0-9896-7AB52DF8CD8A}");

//    int AddRef();
//    int Release();
//    WinNT.HRESULT QueryInterface(Guid.REFIID byValue, PointerByReference pointerByReference);

//    int getRootElement(PointerByReference root);
//    int getElementFromHandle(WinDef.HWND hwnd, PointerByReference element);
//    int createAndCondition(Pointer condition1, Pointer condition2, PointerByReference condition);
//    int createPropertyCondition(int propertyId, Variant.VARIANT.ByValue value, PointerByReference condition);
//    int createOrCondition(Pointer condition1, Pointer condition2, PointerByReference condition);
//    int createTrueCondition(PointerByReference condition);
//    int createFalseCondition(PointerByReference condition);
//    int compareElements(Pointer element1, Pointer element2, IntByReference same);
//    int createNotCondition(Pointer condition, PointerByReference retval);
//    int getPatternProgrammaticName(int patternId, PointerByReference retval);
//    int getFocusedElement(PointerByReference element);
//    int createTreeWalker(PointerByReference condition, PointerByReference walker);
//    int getControlViewWalker(PointerByReference walker);
//    int addAutomationEventHandler(IntByReference eventId, TreeScope scope, Pointer element, PointerByReference cacheRequest, PointerByReference handler);
//    int removeAutomationEventHandler(IntByReference eventId, PointerByReference element, PointerByReference handler);
//    int elementFromPoint(WinDef.POINT pt, PointerByReference element);
}

