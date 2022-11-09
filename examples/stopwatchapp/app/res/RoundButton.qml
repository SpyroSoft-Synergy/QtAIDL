// Copyright (C) 2022 SpyroSoft Synergy S.A.
// SPDX-License-Identifier: MIT
import QtQuick

Rectangle {
    property bool pressed: false

    signal clicked

    height: width
    radius: width * 0.5
    width: 52

    Image {
        anchors.centerIn: parent
        source: "timer_components/button_effect.png"
        visible: pressed ? true : false
    }
    MouseArea {
        acceptedButtons: Qt.LeftButton
        anchors.fill: parent

        onClicked: parent.clicked()
        onPressed: {
            parent.pressed = true;
        }
        onReleased: {
            parent.pressed = false;
        }
    }
}
