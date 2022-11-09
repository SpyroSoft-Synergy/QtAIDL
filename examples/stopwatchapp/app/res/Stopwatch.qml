// Copyright (C) 2022 SpyroSoft Synergy S.A.
// SPDX-License-Identifier: MIT
import QtQuick
import QtQuick.Layouts
import com.spyro_soft.stopwatchservice 1.0

Image {
    id: root

    readonly property real angle: root.milliseconds / 1000 * 6
    readonly property real milliseconds: controller.time.minutes * 60 * 1000 + controller.time.seconds * 1000 + controller.time.milliseconds

    source: "timer_components/background.png"

    StopwatchController {
        id: controller
    }
    Image {
        anchors.horizontalCenter: clock_face.horizontalCenter
        anchors.top: clock_face.top
        anchors.topMargin: 58
        source: "timer_components/clock_face_shadow.png"
        width: clock_face.width
    }
    CircularItem {
        anchors.centerIn: clock_face
        height: width
        revealAngle: root.angle
        width: parent.width

        Behavior on revealAngle  {
            SmoothedAnimation {
                velocity: 100
            }
        }

        Image {
            anchors.centerIn: parent
            opacity: root.milliseconds / 10000.0
            rotation: root.angle
            source: "timer_components/minute_glow.png"
            visible: root.angle

            Behavior on opacity  {
                SmoothedAnimation {
                    duration: 1000
                    velocity: -1
                }
            }
            Behavior on rotation  {
                SmoothedAnimation {
                    velocity: 100
                }
            }
            Behavior on visible  {
                SmoothedAnimation {
                    duration: 2000
                    velocity: -1
                }
            }
        }
    }
    Image {
        id: clock_face
        anchors.centerIn: parent
        source: "timer_components/clock_face.png"

        RowLayout {
            anchors.centerIn: parent
            spacing: 2

            Text {
                color: "#FFFFFF"
                font.family: b612.font.family
                font.pixelSize: 52
                font.weight: b612.font.weight
                text: ("0" + controller.time.minutes).slice(-2)
            }
            Text {
                bottomPadding: 20
                color: "#FFFFFF"
                font.family: b612.font.family
                font.pixelSize: 52
                font.weight: b612.font.weight
                text: ":"
            }
            Text {
                color: "#FFFFFF"
                font.family: b612.font.family
                font.pixelSize: 52
                font.weight: b612.font.weight
                text: ("0" + controller.time.seconds).slice(-2)
            }
            Text {
                Layout.bottomMargin: -4
                color: "#72AAFF"
                font.family: b612.font.family
                font.pixelSize: 40
                font.weight: b612.font.weight
                text: "."
            }
            Text {
                Layout.bottomMargin: -4
                color: "#72AAFF"
                font.family: b612.font.family
                font.pixelSize: 40
                font.weight: b612.font.weight
                text: (controller.time.milliseconds / 1000).toFixed(2).slice(-2)

                Image {
                    anchors.centerIn: parent
                    opacity: 0.5
                    source: "timer_components/flow_mili.png"
                }
            }
        }
    }
    CircularItem {
        anchors.centerIn: clock_face
        height: width
        revealAngle: root.angle
        width: clock_face.width

        Behavior on revealAngle  {
            SmoothedAnimation {
                velocity: 100
            }
        }

        Image {
            anchors.centerIn: parent
            // Unveil minute gauge for the first 10 seconds
            opacity: root.milliseconds / 10000.0
            rotation: root.angle
            source: "timer_components/minute_gouge.png"

            Behavior on opacity  {
                SmoothedAnimation {
                    duration: 1000
                    velocity: -1
                }
            }
            Behavior on rotation  {
                SmoothedAnimation {
                    velocity: 100
                }
            }
        }
    }
    Image {
        anchors.centerIn: clock_face
        source: "timer_components/markers.png"
    }
    Image {
        anchors.horizontalCenter: clock_face.horizontalCenter
        anchors.top: clock_face.top
        anchors.topMargin: 90
        fillMode: Image.PreserveAspectFit
        source: "timer_components/spyro_logo.png"
        width: parent.width * 0.1
    }
    RowLayout {
        anchors.bottom: clock_face.bottom
        anchors.bottomMargin: 90
        anchors.horizontalCenter: parent.horizontalCenter
        spacing: 32

        RoundButton {
            id: reset
            border.color: reset.pressed ? "#05CB78" : "#72AAFF"
            border.width: 2
            color: "transparent"

            onClicked: {
                controller.reset();
            }

            Rectangle {
                anchors.centerIn: parent
                color: reset.pressed ? "#05CB78" : "#72AAFF"
                height: width
                radius: 3
                width: 16
            }
        }
        RoundButton {
            id: playButton
            color: "#05CB78"

            onClicked: {
                controller.isRunning = !controller.isRunning;
            }

            Image {
                id: img
                anchors.left: parent.left

                /* Non symetric play icon, to make it appear centered - has to be moved a little to the right */
                anchors.leftMargin: controller.isRunning ? (parent.width - width) / 2 : (parent.width * 1.05 - width) / 2
                anchors.verticalCenter: parent.verticalCenter
                fillMode: Image.PreserveAspectFit
                source: controller.isRunning ? "timer_components/stop.png" : "timer_components/play.png"
                width: parent.width * 0.25
            }
        }
        FontLoader {
            id: b612
            source: "timer_components/B612-Regular.ttf"
        }
    }
}
