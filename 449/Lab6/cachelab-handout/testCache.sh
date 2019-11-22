#!/bin/bash
if [ -f ./cache-test ]
then
  rm cache-test
fi

echo
echo "Testing Caches"
echo
echo
echo "cache_1c_1e_1k:"
echo
rm cache-test 1> /dev/null
make cache-test TEST_CACHE=caches/cache_1c_1e_1k.o
./cache-test
echo
echo "cache_16384c_4e_4k.o:"
echo
rm cache-test 1> /dev/null
make cache-test TEST_CACHE=caches/cache_16384c_4e_4k.o
./cache-test
echo
echo "cache_32768c_8e_8k.o"
echo
rm cache-test 1> /dev/null
make cache-test TEST_CACHE=caches/cache_32768c_8e_8k.o
./cache-test
echo
echo "cache_65536c_2e_16k.o:"
echo
rm cache-test 1> /dev/null
make cache-test TEST_CACHE=caches/cache_65536c_2e_16k.o
./cache-test
echo
echo "cache_1048576c_256e_256k.o:"
echo
rm cache-test 1> /dev/null
make cache-test TEST_CACHE=caches/cache_1048576c_256e_256k.o
./cache-test
echo
echo "Finished."
echo
rm cache-test 1> /dev/null
