// Copyright (C) 2022 SpyroSoft Synergy S.A.
// SPDX-License-Identifier: MIT
import QtQuick

Rectangle {
    property int borderWidth: 3
    property color gradientColorA: "#ffffff"
    property color gradientColorB: "#dddddd"
    property double gradientStart: 0.0
    property double gradientStop: 1.0
    property bool inverted: false

    gradient: inverted ? invertedGradient : normalGradient

    Gradient {
        id: normalGradient
        GradientStop {
            color: gradientColorA
            position: gradientStart
        }
        GradientStop {
            color: gradientColorB
            position: gradientStop
        }
    }
    Gradient {
        id: invertedGradient
        GradientStop {
            color: gradientColorB
            position: gradientStart
        }
        GradientStop {
            color: gradientColorA
            position: gradientStop
        }
    }
    Rectangle {
        anchors.centerIn: parent
        gradient: inverted ? normalGradient : invertedGradient
        height: parent.height - (parent.borderWidth * 2)
        radius: parent.radius
        width: parent.width - (parent.borderWidth * 2)
    }
}
