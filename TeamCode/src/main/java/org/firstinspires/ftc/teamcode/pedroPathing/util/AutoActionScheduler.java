package org.firstinspires.ftc.teamcode.pedroPathing.util;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AutoActionScheduler {
    final Queue<Action> actions = new LinkedList<>();
    final FtcDashboard dash = FtcDashboard.getInstance();
    final Canvas canvas = new Canvas();

    public long autoRunElapsedTime = 0;

    int actionOrder = 0;

    private HardwareMap hardwareMap;

    public AutoActionScheduler(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public void run() {
        long startTime = System.currentTimeMillis();

        while (actions.peek() != null && !Thread.currentThread().isInterrupted()) {
            TelemetryPacket packet = new TelemetryPacket();
            packet.fieldOverlay().getOperations().addAll(canvas.getOperations());


            Action a = actions.peek();
            a.preview(canvas);

            boolean running =  a.run(packet);
            dash.sendTelemetryPacket(packet);

            if (!running) {
                //Log.d("AutoScheduler_Logger", a + " is removed");
                actions.remove();
            }

//         List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);
//         for (LynxModule module : allHubs) {
//            module.clearBulkCache();
//         }
        }

        autoRunElapsedTime = System.currentTimeMillis() - startTime;

    }

    public boolean isEmpty() {
        return actions.isEmpty();
    }

    public void reset() {
        while (actions.peek() != null && !Thread.currentThread().isInterrupted()) {
            actions.remove();
        }
    }

    public int size() {
        return actions.size();
    }
}