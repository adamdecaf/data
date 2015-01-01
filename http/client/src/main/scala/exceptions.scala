package data.http

case object TooManyRetriesException extends Exception("Request is still failing after exhaustive retry attempts.")
