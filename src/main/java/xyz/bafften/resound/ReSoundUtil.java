package xyz.bafften.resound;

import xyz.duncanruns.julti.Julti;
import xyz.duncanruns.julti.management.InstanceManager;

public class ReSoundUtil {
    private ReSoundUtil() {
    }

    public static int instances;

    public static void getInstanceCount() {
        synchronized (Julti.getJulti()) {
            ReSoundUtil.instances = InstanceManager.getInstanceManager().getSize();
        }
    }

    public static void jultiVolumeOff(){

    }

    public static void jultiVolumeOn(ReSoundOptions options){

    }

}