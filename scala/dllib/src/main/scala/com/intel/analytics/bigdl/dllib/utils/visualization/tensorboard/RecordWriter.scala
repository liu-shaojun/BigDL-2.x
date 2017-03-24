/*
 * Copyright 2016 The BigDL Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intel.analytics.bigdl.visualization.tensorboard

import java.io.{File, FileOutputStream}

import com.google.common.primitives.{Ints, Longs}
import netty.Crc32c
import org.tensorflow.util.Event

/**
 * A writer to write event protobuf to file by tensorboard's format.
 * @param file
 */
private[bigdl] class RecordWriter(file: File) {
  val outputStream = new FileOutputStream(file)
  val crc32 = new Crc32c()
  def write(event: Event): Unit = {
    val eventString = event.toByteArray
    val header = Longs.toByteArray(eventString.length.toLong).reverse
    outputStream.write(header)
    outputStream.write(Ints.toByteArray(maskedCRC32(header).toInt).reverse)
    outputStream.write(eventString)
    outputStream.write(Ints.toByteArray(maskedCRC32(eventString).toInt).reverse)
  }

  def close(): Unit = {
    outputStream.close()
  }

  def maskedCRC32(data: Array[Byte]): Long = {
    crc32.reset()
    crc32.update(data, 0, data.length)
    val x = u32(crc32.getValue)
    u32(((x >> 15) | u32(x << 17)) + 0xa282ead8)
  }

  def u32(x: Long): Long = {
    x & 0xffffffff
  }
}
