// Copyright (C) 2022 SpyroSoft Synergy S.A.
// SPDX-License-Identifier: MIT
import QtQuick

Rectangle {
    property bool pressed: false
    property string text: "Button"

    signal clicked

    height: 30
    radius: 15
    width: 70

    RaisedBackground {
        anchors.fill: parent
        borderWidth: 1
        gradientStart: 0.5
        inverted: !pressed
        radius: parent.radius
    }
    Text {
        anchors.fill: parent
        color: "#000000"
        horizontalAlignment: Text.AlignHCenter
        text: parent.text
        verticalAlignment: Text.AlignVCenter
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
