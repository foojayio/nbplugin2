package io.foojay.support;

import org.netbeans.spi.java.platform.CustomPlatformInstall;
import org.openide.WizardDescriptor;
import org.openide.WizardDescriptor.InstantiatingIterator;

public class FoojayPlatformInstall extends CustomPlatformInstall {

    private FoojayPlatformInstall() {}
    
    @Override
    public InstantiatingIterator<WizardDescriptor> createIterator() {
        return new FoojayPlatformIt();
    }

    @Override
    public String getDisplayName() {
        return "Remote Universal OpenJDK Service";
    }
    
    public static FoojayPlatformInstall create() {
        return new FoojayPlatformInstall();
    }
    
}
