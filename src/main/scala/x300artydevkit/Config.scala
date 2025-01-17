// See LICENSE for license details.
// Modifications copyright (C) 2018-2019 Hex-Five
package hexfive.x300artydevkit

import freechips.rocketchip.config._
import freechips.rocketchip.subsystem._
import freechips.rocketchip.devices.debug._
import freechips.rocketchip.devices.tilelink._
import freechips.rocketchip.diplomacy.{DTSModel, DTSTimebase}
import freechips.rocketchip.system._
import freechips.rocketchip.tile._
import RoCCPrac._
import sifive.blocks.devices.mockaon._
import sifive.blocks.devices.gpio._
import sifive.blocks.devices.pwm._
import sifive.blocks.devices.spi._
import sifive.blocks.devices.uart._
import sifive.blocks.devices.i2c._

class WithITIMAddr(addr: BigInt) extends Config((site, here, up) =>
{
  case RocketTilesKey => up(RocketTilesKey, site) map { r =>
    r.copy(icache = r.icache.map(_.copy(itimAddr = Some(addr)))) }
})

class WithUserMode() extends Config((site, here, up) =>
{
  case RocketTilesKey => up(RocketTilesKey, site) map { r =>
    r.copy(core = r.core.copy(useUser = true)) }
})

class WithLocalInterrupts(nLocalInterrupts: Int) extends Config((site, here, up) =>
{
  case RocketTilesKey => up(RocketTilesKey, site) map { r =>
    r.copy(core = r.core.copy(nLocalInterrupts = nLocalInterrupts)) }
})

class WithPerfCounters(nPerfCounters: Int) extends Config((site, here, up) =>
{
  case RocketTilesKey => up(RocketTilesKey, site) map { r =>
    r.copy(core = r.core.copy(nPerfCounters = nPerfCounters)) }
})

class WithDTSTimebase(timebase: Int) extends Config((site, here, up) =>
{
  case DTSTimebase => BigInt(timebase)
})

class WithMVendorID(mvendorid: Int) extends Config((site, here, up) =>
{
  case RocketTilesKey => up(RocketTilesKey, site) map { r =>
    r.copy(core = r.core.copy(mvendorid = mvendorid)) }
})

class WithJtagDTMConfig(idcodeVersion: Int, idcodePartNum: Int, idcodeManufId: Int, debugIdleCycles: Int) extends Config((site, here, up) =>
{
  case JtagDTMKey => new JtagDTMConfig (
    idcodeVersion = idcodeVersion,
    idcodePartNum = idcodePartNum,
    idcodeManufId = idcodeManufId,
    debugIdleCycles = debugIdleCycles)
})

// X300ArtyDevKit Peripherals
class X300ArtyDevKitPeripherals extends Config((site, here, up) => {
  case PeripheryGPIOKey => List(
    GPIOParams(address = 0x10012000, width = 32, includeIOF = true))
  case PeripheryPWMKey => List(
    PWMParams(address = 0x10015000, cmpWidth = 8),
    PWMParams(address = 0x10025000, cmpWidth = 16),
    PWMParams(address = 0x10035000, cmpWidth = 16))
  case PeripherySPIKey => List(
    SPIParams(csWidth = 4, rAddress = 0x10024000, defaultSampleDel = 3),
    SPIParams(csWidth = 1, rAddress = 0x10034000, defaultSampleDel = 3))
  case PeripherySPIFlashKey => List(
    SPIFlashParams(
      fAddress = 0x20000000,
      rAddress = 0x10014000,
      defaultSampleDel = 3))
  case PeripheryUARTKey => List(
    UARTParams(address = 0x10013000),
    UARTParams(address = 0x10023000))
  case PeripheryI2CKey => List(
    I2CParams(address = 0x10016000))
  case PeripheryMockAONKey =>
    MockAONParams(address = 0x10000000)
  case PeripheryMaskROMKey => List(
    MaskROMParams(address = 0x10000, name = "BootROM"))
})

// X300ArtyDevKit Config
class X300ArtyDevKitConfig extends Config(
  new X300ArtyDevKitPeripherals ++
  new WithDTSTimebase(32768)    ++
  new WithJtagDTMConfig(
      idcodeVersion = 1,
      idcodePartNum = 0x300,
      idcodeManufId = 0x57C,
      debugIdleCycles = 5)      ++
  new WithMVendorID(0x57C)      ++
  new WithNBreakpoints(8)       ++
  new WithNExtTopInterrupts(0)  ++
  new WithJtagDTM               ++
  new WithNoMemPort             ++
  new WithNoMMIOPort            ++
  new WithNMemoryChannels(0)    ++
  new WithNBanks(0)             ++
  new WithL1ICacheWays(4)       ++
  new WithL1DCacheSets(1024)    ++
  new WithUserMode              ++
  new WithITIMAddr(0x08000000)  ++
  new WithLocalInterrupts(3)    ++
  new WithPerfCounters(2)       ++
  new With1TinyCore             ++
  new BaseConfig
)
class X300MyRoCC2 extends Config(
  new X300ArtyDevKitPeripherals ++
  new WithDTSTimebase(32768)    ++
  new WithJtagDTMConfig(
      idcodeVersion = 1,
      idcodePartNum = 0x300,
      idcodeManufId = 0x57C,
      debugIdleCycles = 5)      ++
  new WithMVendorID(0x57C)      ++
  new WithNBreakpoints(8)       ++
  new WithNExtTopInterrupts(0)  ++
  new WithJtagDTM               ++
  new WithNoMemPort             ++
  new WithNoMMIOPort            ++
  new WithNMemoryChannels(0)    ++
  new WithNBanks(0)             ++
  new WithL1ICacheWays(4)       ++
  new WithL1DCacheSets(1024)    ++
  new WithUserMode              ++
  new WithITIMAddr(0x08000000)  ++
  new WithLocalInterrupts(3)    ++
  new WithPerfCounters(2)       ++
    // agrega la Config
    new WithMyRoCC2 ++
  new With1TinyCore             ++
  new BaseConfig
)