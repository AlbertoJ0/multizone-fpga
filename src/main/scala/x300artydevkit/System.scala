// See LICENSE for license details.
// Modifications copyright (C) 2018-2019 Hex-Five
package hexfive.x300artydevkit

import Chisel._

import freechips.rocketchip.config._
import freechips.rocketchip.subsystem._
import freechips.rocketchip.devices.debug._
import freechips.rocketchip.devices.tilelink._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.interrupts._
import freechips.rocketchip.system._

import sifive.blocks.devices.mockaon._
import sifive.blocks.devices.gpio._
import sifive.blocks.devices.pwm._
import sifive.blocks.devices.spi._
import sifive.blocks.devices.uart._
import sifive.blocks.devices.i2c._

import sifive.fpgashells.devices.xilinx.xilinxethernetlite._

//-------------------------------------------------------------------------
// X300ArtyDevKitSystem
//-------------------------------------------------------------------------

class X300ArtyDevKitSystem(implicit p: Parameters) extends RocketSubsystem
    with HasPeripheryMaskROMSlave
    with HasPeripheryDebug
    with HasPeripheryMockAON
    with HasPeripheryUART
    with HasPeripherySPIFlash
    with HasPeripherySPI
    with HasPeripheryPWM
    with HasPeripheryI2C
    with HasSystemXilinxEthernetLite {
  override lazy val module = new X300ArtyDevKitSystemModule(this)

  val gpio = p(PeripheryGPIOKey).map { ps =>
    GPIO.attach(GPIOAttachParams(ps, pbus, ibus.fromAsync))
  }

  val gpioNodes = gpio.map { g =>
    g.ioNode.makeSink
  }

  private val device = new Device with DeviceInterrupts {
    def describe(resources: ResourceBindings): Description = {
      Description("soc/local-interrupts", describeInterrupts(resources))
    }
  }

  val clicInterrupts = IntSourceNode(IntSourcePortSimple(num = 3, resources = device.int))

  val asyncXing = LazyModule(new IntXing(3))
  asyncXing.intnode := clicInterrupts
  tiles(0).crossIntIn() := asyncXing.intnode
}

class X300ArtyDevKitSystemModule[+L <: X300ArtyDevKitSystem](_outer: L)
  extends RocketSubsystemModuleImp(_outer)
    with HasPeripheryDebugModuleImp
    with HasPeripheryUARTModuleImp
    with HasPeripherySPIModuleImp
    with HasPeripherySPIFlashModuleImp
    with HasPeripheryMockAONModuleImp
    with HasPeripheryPWMModuleImp
    with HasPeripheryI2CModuleImp
    with HasSystemXilinxEthernetLiteModuleImp {
  // Reset vector is set to the location of the mask rom
  val maskROMParams = p(PeripheryMaskROMKey)
  global_reset_vector := maskROMParams(0).address.U

  val gpio = outer.gpioNodes.zipWithIndex.map { case(n,i) => n.makeIO()(ValName(s"gpio_$i")) }

  outer.clicInterrupts.out.map(_._1).flatten.zipWithIndex.foreach {
    case(o, 0) => o := outer.gpio(0).module.lip(15)
    case(o, 1) => o := outer.gpio(0).module.lip(30)
    case(o, 2) => o := outer.gpio(0).module.lip(31)
  }
}
