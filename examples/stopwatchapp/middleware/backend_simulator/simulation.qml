// Copyright (C) 2022 SpyroSoft Synergy S.A.
// SPDX-License-Identifier: MIT
import QtQuick
import com.spyro_soft.stopwatchservice
import com.spyro_soft.stopwatchservice.simulation

Item {
    id: simulation

    property int elapsedTime: 0

    onElapsedTimeChanged: {
        const miliseconds = elapsedTime % 1000;
        const seconds = Math.floor(elapsedTime / 1000) % 60;
        const minutes = Math.floor(elapsedTime / 60000);
        stopwatchcontroller.time = Stopwatchservice.timeStruct(minutes, seconds, miliseconds);
    }

    StopwatchControllerBackend {
        id: stopwatchcontroller

        property var settings: IfSimulator.findData(IfSimulator.simulationData, "StopwatchController")

        function initialize() {
            print("StopWatchControllerSimulation INITIALIZE");
            IfSimulator.initializeDefault(settings, stopwatchcontroller);
            Base.initialize();
        }
        function reset(reply) {
            print("StopWatchControllerSimulation reset");
            stopwatchcontroller.isRunning = false;
            simulation.elapsedTime = 0;
            reply.setSuccess(true);
        }
        function toggle(reply) {
            print("StopWatchControllerSimulation toggle");
            stopwatchcontroller.isRunning = !stopwatchcontroller.isRunning;
            reply.setSuccess(true);
        }

        Component.onCompleted: {
            console.log("BACKEND SIMULATION CREATED");
        }
    }
    Timer {
        id: timer
        interval: 16
        repeat: true
        running: stopwatchcontroller.isRunning

        onTriggered: simulation.elapsedTime += timer.interval
    }
}
