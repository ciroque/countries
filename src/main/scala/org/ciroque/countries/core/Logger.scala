package org.ciroque.countries.core

import org.slf4j.LoggerFactory

trait Logger {
  def error(entry: String)
  def warning(entry: String)
  def info(entry: String)
  def debug(entry: String)
}

class LogWriter extends Logger {

  private val logger = LoggerFactory.getLogger("CountryService")

  override def error(entry: String): Unit = logger.debug(entry)

  override def warning(entry: String): Unit = logger.warn(entry)

  override def debug(entry: String): Unit = logger.debug(entry)

  override def info(entry: String): Unit = logger.info(entry)
}