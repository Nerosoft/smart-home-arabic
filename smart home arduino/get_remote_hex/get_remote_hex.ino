#include <IRremote.h>
bool on[4]={true,true,true,true};
int RECV_PIN = 12;

IRrecv irrecv(RECV_PIN);

decode_results results;

void setup()
{
  Serial.begin(9600);
  irrecv.enableIRIn(); // Start the receiver
  Serial.println("start");
}


void loop() {
  if (irrecv.decode(&results)) {

    Serial.println(results.value, HEX); // طباعة القيمه الخاصه بالزر داخل الريموت

    irrecv.resume(); // Receive the next value
  }
  delay(100);
}


