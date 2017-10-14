package mmarquee.automation;

import com.sun.jna.platform.win32.*;
import com.sun.jna.platform.win32.COM.Unknown;
import mmarquee.automation.controls.AutomationApplication;
import mmarquee.automation.controls.AutomationPanel;
import mmarquee.automation.pattern.PatternNotFoundException;
import mmarquee.automation.utils.Utils;

import java.util.logging.Logger;

abstract class UIAutomationBase extends BaseAutomation {
    static int FIND_DESKTOP_ATTEMPTS = 25;

    protected AutomationElement rootElement;

    protected Logger logger = Logger.getLogger(UIAutomation.class.getName());

    protected static Ole32Wrapper Ole32 = null;


    /**
     * Gets the underlying unknown value of Ole32.
     * @return Unknown The COM Unknown value.
     */
    Unknown getOle32Unknown() {
        return Ole32.getUnknown();
    }

    /**
     * Launches the application.
     *
     * @param command The command to be called.
     * @return AutomationApplication that represents the application.
     * @throws java.io.IOException Cannot start application?
     * @throws AutomationException Automation library error.
     */
    public AutomationApplication launch(final String... command)
            throws java.io.IOException, AutomationException {
        Process process = Utils.startProcess(command);
        return new AutomationApplication(rootElement, process, false);
    }

    /**
     * Launches the application, from a given directory.
     *
     * @param command The command to be called.
     * @return AutomationApplication that represents the application.
     * @throws java.io.IOException Cannot start application?
     * @throws AutomationException Automation library error.
     */
    public AutomationApplication launchWithDirectory(final String... command)
            throws java.io.IOException, AutomationException {
        Process process = Utils.startProcessWithWorkingDirectory(command);
        return new AutomationApplication(rootElement, process, false);
    }

    /**
     * Attaches to the application process.
     *
     * @param process Process to attach to.
     * @return AutomationApplication that represents the application.
     * @throws AutomationException Automation library error.
     */
    public AutomationApplication attach(final Process process)
            throws AutomationException {
        return new AutomationApplication(rootElement, process, true);
    }

    /**
     * Attaches or launches the application.
     *
     * @param command Command to be started.
     * @return AutomationApplication that represents the application.
     * @throws java.lang.Exception Unable to find process.
     */
    public AutomationApplication launchOrAttach(final String... command)
            throws Exception {
        final Tlhelp32.PROCESSENTRY32.ByReference processEntry =
                new Tlhelp32.PROCESSENTRY32.ByReference();

        boolean found = Utils.findProcessEntry(processEntry, command);

        if (!found) {
            return this.launch(command);
        } else {
            WinNT.HANDLE handle = Utils.getHandleFromProcessEntry(processEntry);
            return new AutomationApplication(rootElement, handle, true);
        }
    }

    /**
     * Attaches or launches the application.
     *
     * @param command Command to be started.
     * @return AutomationApplication that represents the application.
     * @throws java.lang.Exception Unable to find process.
     */
    public AutomationApplication launchWithWorkingDirectoryOrAttach(final String... command)
            throws Exception {
        final Tlhelp32.PROCESSENTRY32.ByReference processEntry =
                new Tlhelp32.PROCESSENTRY32.ByReference();

        boolean found = Utils.findProcessEntry(processEntry, command);

        if (!found) {
            return this.launchWithDirectory(command);
        } else {
            WinNT.HANDLE handle = Utils.getHandleFromProcessEntry(processEntry);
            return new AutomationApplication(rootElement, handle, true);
        }
    }

    /**
     * Gets the main desktop object.
     *
     * @return AutomationPanel The found object.
     * @throws ElementNotFoundException Element is not found.
     * @throws PatternNotFoundException Expected pattern not found.
     */
    public AutomationPanel getDesktop()
            throws AutomationException, PatternNotFoundException {
        return new AutomationPanel(this.rootElement);
    }
}
