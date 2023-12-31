#include <esp32cam.h>
#include <WebServer.h>
#include <WiFi.h>

const char* WIFI_SSID = "马桢的iPhone";  // 改成自己的wifi名称
const char* WIFI_PASS = "1malinzhen";  // 改成自己的wifi密码
int buzzer=0;
WebServer server(80);

static auto loRes = esp32cam::Resolution::find(1600,1200);
static auto hiRes = esp32cam::Resolution::find(1600,1200);

void handleBmp()
{
  if (!esp32cam::Camera.changeResolution(hiRes)) {
    Serial.println("SET-LO-RES FAIL");
  }

  auto frame = esp32cam::capture();
  if (frame == nullptr) {
    Serial.println("CAPTURE FAIL");
    server.send(503, "", "");
    return;
  }
  Serial.printf("CAPTURE OK %dx%d %db\n", frame->getWidth(), frame->getHeight(),
                static_cast<int>(frame->size()));

  if (!frame->toBmp()) {
    server.send(503, "", "");
    return;
  }

  Serial.printf("CONVERT OK %dx%d %db\n", frame->getWidth(), frame->getHeight(),
                static_cast<int>(frame->size()));

  server.setContentLength(frame->size());
  server.send(200, "image/bmp");
  WiFiClient client = server.client();
  frame->writeTo(client);
}

void serveJpg()
{
  auto frame = esp32cam::capture();
  if (frame == nullptr) {
    Serial.println("CAPTURE FAIL");
    server.send(503, "", "");
    return;
  }
  Serial.printf("CAPTURE OK %dx%d %db\n", frame->getWidth(), frame->getHeight(),
                static_cast<int>(frame->size()));

  server.setContentLength(frame->size());
  server.send(200, "image/jpeg");
  WiFiClient client = server.client();
  frame->writeTo(client);
}

void handleJpgLo()
{
  if (!esp32cam::Camera.changeResolution(loRes)) {
    Serial.println("SET-LO-RES FAIL");
  }
  serveJpg();
}

void handleJpgHi()
{
  if (!esp32cam::Camera.changeResolution(hiRes)) {
    Serial.println("SET-HI-RES FAIL");
  }
  serveJpg();
}

void handleJpg()
{
  server.sendHeader("Location", "/cam-hi.jpg");
  server.send(302, "", "");
   Serial.println("handleJpg");
}

void handleMjpeg()
{
  if (!esp32cam::Camera.changeResolution(hiRes)) {
    Serial.println("SET-HI-RES FAIL");
  }

  Serial.println("STREAM BEGIN");
  WiFiClient client = server.client();
  auto startTime = millis();
  int res = esp32cam::Camera.streamMjpeg(client);
  if (res <= 0) {
    Serial.printf("STREAM ERROR %d\n", res);
    return;
  }
  auto duration = millis() - startTime;
  Serial.printf("STREAM END %dfrm %0.2ffps\n", res, 1000.0 * res / duration);
}
void  open_LED ()
{
  server.send(200, "", "");
  digitalWrite(4,HIGH); 
  Serial.printf("LED——打开");
}

void close_LED ()
{
 server.send(200, "", "");
  digitalWrite(4,LOW); 
  Serial.printf("LED——打开");
}
void  handle_cam_on_or_off()
{ 
   buzzer=!buzzer;
   server.send(200, "", "");
   if(buzzer==0)
   Serial.printf("蜂鸣器——关闭");
   if(buzzer==1)
   Serial.printf("蜂鸣器——打开");
}

void test()
{
Serial.printf("test\n");
}
void setup()
{
  pinMode(4,OUTPUT);
  pinMode(13,OUTPUT);
  Serial.begin(115200);
  Serial.println();

  {
    using namespace esp32cam;
    Config cfg;
    cfg.setPins(pins::AiThinker);
    cfg.setResolution(hiRes);
    cfg.setBufferCount(2);
    cfg.setJpeg(92);

    bool ok = Camera.begin(cfg);
    Serial.println(ok ? "CAMERA OK" : "CAMERA FAIL");
  }

  WiFi.persistent(false);
  WiFi.mode(WIFI_STA);
  WiFi.begin(WIFI_SSID, WIFI_PASS);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
  }

  Serial.print("http://");
  Serial.println(WiFi.localIP());
  Serial.println("  /cam.bmp");
  Serial.println("  /cam-lo.jpg");
  Serial.println("  /cam-hi.jpg");
  Serial.println("  /cam.mjpeg");

  server.on("/cam.bmp", handleBmp);
  server.on("/cam-lo.jpg", handleJpgLo);
  server.on("/cam-hi.jpg", handleJpgHi);
  server.on("/cam.jpg", handleJpg);
  server.on("/cam.mjpeg", handleMjpeg);
  server.on("/cam_on_or_off", handle_cam_on_or_off);
  server.on("/open_LED", open_LED);
  server.on("/close_LED",close_LED);
  server.on("/test", test);
  server.begin();
}
void loop()
{
 if(buzzer==1)
 {
    int i=0;
      while(i<=500)
  {
    i++;
    digitalWrite(13,HIGH);  
    delay(1);
    digitalWrite(13,LOW);
    delay(1);
  }
 }
  server.handleClient();
}
