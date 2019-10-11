namespace java com.snowplowanalytics.snowplow.collectors.thrift

enum PayloadProtocol {
  Http = 1
}

enum PayloadFormat {
  HttpGet = 1
  HttpPostUrlencodedForm = 10
  HttpPostMultipartForm = 11
}

typedef string PayloadData

struct TrackerPayload {
  1: PayloadProtocol protocol
  2: PayloadFormat format
  3: PayloadData data
}

struct SnowplowRawEvent {
  01: i64 timestamp // Milliseconds since epoch.
  20: string collector // Collector name/version.
  30: string encoding
  40: string ipAddress
  41: optional TrackerPayload payload
  45: optional string hostname
  50: optional string userAgent
  60: optional string refererUri
  70: optional list<string> headers
  80: optional string networkUserId
}