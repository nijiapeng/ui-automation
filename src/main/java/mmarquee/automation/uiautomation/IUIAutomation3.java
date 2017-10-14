package mmarquee.automation.uiautomation;

import com.sun.jna.Function;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.COM.IUnknown;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * Automation interface for Windows 8.1 and above.
 *
 * @author Mark Humphreys
 * Date 12/09/2016.
 *
 */
public interface IUIAutomation3 extends IUnknown {
    /**
     * The interface IID for QueryInterface et al
     */
    Guid.IID IID = new Guid.IID("{73D768DA-9B51-4B89-936E-C209290973E7}");

    int AddRef();
    int Release();
    WinNT.HRESULT QueryInterface(Guid.REFIID byValue, PointerByReference pointerByReference);

    int getRootElement(PointerByReference root);
    int getElementFromHandle(WinDef.HWND hwnd, PointerByReference element);
    int createAndCondition(Pointer condition1, Pointer condition2, PointerByReference condition);
    int createPropertyCondition(int propertyId, Variant.VARIANT.ByValue value, PointerByReference condition);
    int createOrCondition(Pointer condition1, Pointer condition2, PointerByReference condition);
    int createTrueCondition(PointerByReference condition);
    int createFalseCondition(PointerByReference condition);
    int compareElements(Pointer element1, Pointer element2, IntByReference same);
    int createNotCondition(Pointer condition, PointerByReference retval);
    int getPatternProgrammaticName(int patternId, PointerByReference retval);
    int getFocusedElement(PointerByReference element);
    int createTreeWalker(PointerByReference condition, PointerByReference walker);
    int getControlViewWalker(PointerByReference walker);
    int addAutomationEventHandler(IntByReference eventId, TreeScope scope, Pointer element, PointerByReference cacheRequest, PointerByReference handler);
    int removeAutomationEventHandler(IntByReference eventId, PointerByReference element, PointerByReference handler);
    int elementFromPoint(WinDef.POINT pt, PointerByReference element);
}

