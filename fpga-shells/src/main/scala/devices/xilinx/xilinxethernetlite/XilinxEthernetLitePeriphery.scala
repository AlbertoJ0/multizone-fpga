// See LICENSE for license details.
// Copyright (C) 2018-2019 Hex-Five
package sifive.fpgashells.devices.xilinx.xilinxethernetlite

import Chisel._
import freechips.rocketchip.diplomacy.{LazyModule, LazyModuleImp, BufferParams}
import freechips.rocketchip.subsystem.BaseSubsystem
import freechips.rocketchip.tilelink._
import freechips.rocketchip.interrupts.IntSyncCrossingSink

import sifive.fpgashells.ip.xilinx.ethernetlite.{PhyPort, MdioPort}

trait HasSystemXilinxEthernetLite { this: BaseSubsystem =>
  val xilinxethernetlite = LazyModule(new XilinxEthernetLite)
  private val cname = "xilinxethernetlite"
  sbus.coupleTo(s"slave_named_$cname") { xilinxethernetlite.crossTLIn(xilinxethernetlite.slave) :*= TLWidthWidget(sbus.beatBytes) :*= _ }
  ibus.fromSync := xilinxethernetlite.crossIntOut(xilinxethernetlite.intnode)
}

trait HasSystemXilinxEthernetLiteModuleImp extends LazyModuleImp {
  val outer: HasSystemXilinxEthernetLite
  val phy = IO(new Bundle with PhyPort with MdioPort {})

  phy <> outer.xilinxethernetlite.module.io.port
}
