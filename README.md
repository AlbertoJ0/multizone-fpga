# X300

This repository contains the X300, a secure version of [SiFive's Freedom E300
Platform](https://github.com/sifive/freedom/tree/3624efff1819e52cec30c72f9085158189f8b53f)
modified to work with the free and open [MultiZone Secure IoT Stack](https://github.com/hex-five/multizone-secure-iot-stack).

Feature comparison between E300 (not secure) and X300 (secure):

| E300             | X300                                         |
| ---------------- | -------------------------------------------- |
| RV32ACIM         | RV32ACIMU                                    |
| 32.5 MHz clock   | 65 MHz clock                                 |
| 2 HW breakpoints | 8 HW breakpoints                             |
| no Ethernet core | Xilinx EthernetLite Ethernet core            |
| 1-way icache     | 4-way icache                                 |
| no ITIM          | ITIM at 0x0800\_0000                         |
| 16 kB DTIM       | 64 kB DTIM                                   |
| no perf counters | 2 perf counters, hpmcounter3 and hpmcounter4 |
| no CLICs         | 3 CLICs (BTN0, BTN1 and BTN2)                |

Like the Freedom E300 Arty FPGA Dev Kit, The X300 is designed to be mapped onto an [Artix-7 35T Arty FPGA Evaluation Kit](https://www.xilinx.com/products/boards-and-kits/arty.html).


### Bootrom

The default bootrom consists of a program that immediately jumps to address
0x20400000, which is 0x00400000 bytes into the SPI flash memory on the Arty
board.

## Requirements

### Vivado 2017.1 (or later)

You'll need Vivado to synthesize the bistream for the Arty FPGA. You should
have received a Xilinx single node license and instructions how to install Vivado with
your Arty FPGA Dev Kit.

### RISC-V Toolchain

To compile the bootloader for the X300, the RISC-V software toolchain must be
installed locally and the $(RISCV) environment variable must point to the
location of where the RISC-V toolchains are installed. We recommend you build
the toolchain yourself from
[riscv/riscv-gnu-toolchain](https://github.com/riscv/riscv-gnu-toolchain/tree/411d1345507e5313c3575720f128be9e6c0ed941)

Run the following commands to clone the repository and get started:

```sh
$ git clone https://github.com/hex-five/multizone-fpga.git
$ cd multizone-fpga
$ git submodule update --init --recursive
```

### Additional requirements

More info about Chisel requirements like Scala and cbt can be found at [https://github.com/sifive/freedom/blob/master/README.md](https://github.com/sifive/freedom/blob/master/README.md)


## Building

To compile the bistream, run

```sh
$ make -f Makefile.x300artydevkit mcs
```

These will place the files under `builds/x300artydevkit/obj`.

Note that in order to run the `mcs` target, you need to have the `vivado`
executable on your `PATH`.

## Running

For instructions for getting the generated image onto an FPGA and programming
it with software using the [Freedom E
SDK](https://github.com/sifive/freedom-e-sdk), please see the [Freedom E310
Arty FPGA Dev Kit Getting Started
Guide](https://www.sifive.com/documentation/freedom-soc/freedom-e300-arty-fpga-dev-kit-getting-started-guide/).
