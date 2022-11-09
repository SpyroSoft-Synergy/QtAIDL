// Copyright (C) 2022 SpyroSoft Synergy S.A.
// SPDX-License-Identifier: MIT
import QtQuick

Item {
    id: root

    property real edge: 8.0
    property real revealAngle: 0

    layer.enabled: true
    layer.smooth: true

    layer.effect: ShaderEffect {
        property real edge: root.edge
        property real revealAngle: root.revealAngle

        fragmentShader: "shaders/circularShader.frag.qsb"
    }
}
