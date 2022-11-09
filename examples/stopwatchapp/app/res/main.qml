// Copyright (C) 2022 SpyroSoft Synergy S.A.
// SPDX-License-Identifier: MIT
import QtQuick 2.15
import QtQuick.Window 2.15
import com.spyro_soft.stopwatchservice 1.0

Window {
    height: 600
    visible: true
    width: 800

    Stopwatch {
        id: stopwatch
        anchors.fill: parent
    }
}
