# X300

This repository contains the hardware design source files of the Hex Five X300 RISC-V SoC. The X300 SoC is Hex Five's official reference platform for its [MultiZone Security Trusted Execution Environment](https://github.com/hex-five/multizone-sdk) and [MultiZone Security Trusted Firmware](https://github.com/hex-five/multizone-iot-sdk). The X300 is an enhanced secure version of the - now archived - [SiFive's Freedom E300 Platform](https://github.com/sifive/freedom) built around the RISC-V Rocket chip originally developed at U.C. Berkeley.

Feature comparison between E300 (not secure) and X300 (secure):

| E300             | X300                                         |
| ---------------- | -------------------------------------------- |
| RV32IMAC         | RV32IMACU - 'U' mode support for TEE         |
| no PMP           | 8 Physical Memory Protection registers       |
| 32.5 MHz clock   | 65 MHz clock                                 |
| 2 HW breakpoints | 8 HW breakpoints                             |
| no Ethernet core | Xilinx EthernetLite Ethernet 10/100 core     |
| 1-way icache     | 4-way icache                                 |
| no ITIM          | 16KB ITIM at 0x08000000                      |
| 16 KB DTIM       | 64KB DTIM at 0x80000000                      |
| no perf counters | 2 perf counters: hpmcounter3 and hpmcounter4 |
| no BTN mappings  | 3 CLINT sources: BTN0, BTN1, and BTN2        |

Like the Freedom E300 Arty FPGA Dev Kit, the X300 is designed to work with the [Digilent Arty A7 FPGA Evaluation Kit](https://digilent.com/reference/programmable-logic/arty-a7/start) in the 35T or 100T version.

### Bootrom

The default bootrom consists of a program that immediately jumps to address 0x20400000, which is 0x00400000 bytes into the SPI flash memory on the Arty board. The provided bitstream files includes no firmware. A fully functional state-of-the-art secure firmware stack for this device is available at https://github.com/hex-five/multizone-iot-sdk. 

### Quick Start

Prebuilt bitstream files are provided as release assets ready to download and program.

- [X300ArtyA7-35T.mcs](https://github.com/hex-five/multizone-fpga/releases/download/v2.0.0/X300ArtyA7-35T.mcs)
- [X300ArtyA7-100T.mcs](https://github.com/hex-five/multizone-fpga/releases/download/v2.0.0/X300ArtyA7-100T.mcs)

If you don't need to modify the hardware design, you can skip the next sections and jump directly to the `Program` section.

## Requirements

### Xilinx Vivado Design Suite

You need the [Xilinx Vivado Design Suite](https://www.xilinx.com/support/download.html) to synthesize and/or upload the bistream for the Arty FPGA. You should have received a Xilinx single node license and instructions how to install Vivado with your Arty FPGA Dev Kit. If you are not interested in hardware development and just need to flash the X300 bitstream to run RISC-V firmware, you may prefer the Vivado Lab edition, which is smaller, quicker to download and install, and doesn't require license.

### RISC-V Toolchain

To compile the bootloader for the X300, the RISC-V software toolchain must be installed locally and the `RISCV` environment variable must point to the location of the install. We recommend Hex Five's reference build freely available for download at https://hex-five.com/wp-content/uploads/riscv-openocd-20210618.tar.xz or you can use the one included in the rocket-chip submodule.

```
wget https://hex-five.com/wp-content/uploads/riscv-gnu-toolchain-20210618.tar.xz
tar -xvf riscv-gnu-toolchain-20210618.tar.xz
```

## Build

Run the following commands to clone repository and submodules:

```
$ git clone https://github.com/hex-five/multizone-fpga.git
$ cd multizone-fpga
$ git submodule update --init --recursive --jobs 8
```

In order to make the `mcs` target, you need the Vivado executable on your `PATH` and the `RISCV` environment variable pointing to your local toolchain. Change these values according to your setup:
```
export PATH=$PATH:~/Xilinx/Vivado/2021.1/bin
export RISCV=~/riscv-gnu-toolchain-20210618

```

To build the bitstream, run one of these two scripts according to your target:

```
$ make -f Makefile.x300arty35devkit mcs
```
or
```
$ make -f Makefile.x300arty100devkit mcs
```

*Note: if the first build ends prematurely after resolving Scala dependencies, just reenter the command a second time.*


These will place the bitstream file `X300ArtyDevKitFPGAChip.mcs` under `builds/x300artyXXXdevkit/obj`.

## Program

To program the SPI flash with Vivado:
- Launch Vivado
- Open Hardware Manager, click the auto-connect icon, and open the target board
- Right click on the FPGA device and select ”Add Configuration Memory Device”
- Select Part "s25fl128sxxxxxx0-spi-x1_x2_x4" ("mt25ql128-spi-x1_x2_x4" if you have an old Arty 35T)
- Click OK to ”Do you want to program the configuration memory device now?”
- Add X300ArtyA7-35T.mcs or X300ArtyA7-100T.mcs depending on your board
- Select OK
- Once the programming completes in Vivado, press the “PROG” Button on the Arty board to
load the image into the FPGA
