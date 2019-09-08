#line 145 "bits.c"
int bitXor(int x, int y) {
 int xandy=  x & y;
 int xnandy=  ~x & ~y;
 return ~xandy & ~xnandy;
}
#line 156
int tmin(void) {
 return 1 <<31;
}
#line 168
int allOddBits(int x) {
 int odd1=(((((  0xAA << 8) | 0xAA) << 8) | 0xAA)  << 8)  | 0xAA;
 int result=  x & odd1;
 int even1=  ~odd1;
 result = result | even1;
 return (!(~result));
}
#line 183
int conditional(int x, int y, int z) {
 return 2;
}
#line 195
int logicalNeg(int x) {
 return 2;
}
#line 211
int floatFloat2Int(unsigned uf) {
 return 2;
}
