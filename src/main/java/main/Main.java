package main;

import benchmark.LockBenhmark;
import dekker.DekkerLock;
import dekker.RaceCond;
import lampord.CounterCheck;
import lampord.LampordLock;
import lampord.CounterCheck;
import lampord.AdvancedLampordLock;

public class Main {
   /**
    * Main
    * @param args
    */
   public static void main(String[] args) {
//      RaceCond rc = new RaceCond();
//      rc.showRaceCondition();
//      rc.showNoRaceCondition(new DekkerLock());


     CounterCheck ch = new CounterCheck();
     ch.showManipulation(new LampordLock(9), 9);
   }
}