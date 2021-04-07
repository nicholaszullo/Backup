# Part A 
Model Architecture:
AlexNet(
  (features): Sequential(
    (0): Conv2d(3, 96, kernel_size=(11, 11), stride=(4, 4))
    (1): ReLU()
    (2): MaxPool2d(kernel_size=3, stride=2, padding=0, dilation=1, ceil_mode=False)
    (3): Conv2d(96, 256, kernel_size=(5, 5), stride=(1, 1), padding=(2, 2))
    (4): ReLU()
    (5): MaxPool2d(kernel_size=3, stride=2, padding=0, dilation=1, ceil_mode=False)
    (6): Conv2d(256, 384, kernel_size=(3, 3), stride=(1, 1), padding=(1, 1))
    (7): ReLU()
    (8): Conv2d(384, 384, kernel_size=(3, 3), stride=(1, 1), padding=(1, 1))
    (9): ReLU()
    (10): Conv2d(384, 256, kernel_size=(3, 3), stride=(1, 1), padding=(1, 1))
    (11): ReLU()
    (12): MaxPool2d(kernel_size=3, stride=2, padding=0, dilation=1, ceil_mode=False)
  )
  (classifier): Sequential(
    (0): Dropout(p=0.5, inplace=False)
    (1): Linear(in_features=9216, out_features=4096, bias=True)
    (2): ReLU()
    (3): Dropout(p=0.5, inplace=False)
    (4): Linear(in_features=4096, out_features=4096, bias=True)
    (5): ReLU()
    (6): Linear(in_features=4096, out_features=4, bias=True)
  )
)

## DOMAIN training
[Epoch 1] train accuracy: 0.6338, loss: 1.1658
[Epoch 1] eval accuracy: 0.7500, loss: 0.4748
[Epoch 2] train accuracy: 0.7552, loss: 0.5585
[Epoch 2] eval accuracy: 0.7707, loss: 0.5065
[Epoch 3] train accuracy: 0.7907, loss: 0.4578
[Epoch 3] eval accuracy: 0.7956, loss: 0.4584
[Epoch 4] train accuracy: 0.8225, loss: 0.4096
[Epoch 4] eval accuracy: 0.8330, loss: 0.3880
[Epoch 5] train accuracy: 0.8226, loss: 0.4114
[Epoch 5] eval accuracy: 0.8392, loss: 0.3862
[Epoch 6] train accuracy: 0.8457, loss: 0.3798
[Epoch 6] eval accuracy: 0.8506, loss: 0.3627
[Epoch 7] train accuracy: 0.8565, loss: 0.3430
[Epoch 7] eval accuracy: 0.8600, loss: 0.3366
[Epoch 8] train accuracy: 0.8537, loss: 0.3589
[Epoch 8] eval accuracy: 0.8434, loss: 0.3808
[Epoch 9] train accuracy: 0.8569, loss: 0.3726
[Epoch 9] eval accuracy: 0.8330, loss: 0.3760
[Epoch 10] train accuracy: 0.8666, loss: 0.3385
[Epoch 10] eval accuracy: 0.8465, loss: 0.3472
[Epoch 11] train accuracy: 0.8748, loss: 0.3003
[Epoch 11] eval accuracy: 0.8693, loss: 0.3287
[Epoch 12] train accuracy: 0.8790, loss: 0.2999
[Epoch 12] eval accuracy: 0.8776, loss: 0.3102
[Epoch 13] train accuracy: 0.8911, loss: 0.2782
[Epoch 13] eval accuracy: 0.8579, loss: 0.4586
[Epoch 14] train accuracy: 0.8881, loss: 0.2764
[Epoch 14] eval accuracy: 0.8776, loss: 0.3009
[Epoch 15] train accuracy: 0.8968, loss: 0.2561
[Epoch 15] eval accuracy: 0.8817, loss: 0.2814
[Epoch 16] train accuracy: 0.8991, loss: 0.2492
[Epoch 16] eval accuracy: 0.8859, loss: 0.2954
[Epoch 17] train accuracy: 0.9075, loss: 0.2305
[Epoch 17] eval accuracy: 0.8828, loss: 0.3057
[Epoch 18] train accuracy: 0.9083, loss: 0.2248
[Epoch 18] eval accuracy: 0.8807, loss: 0.2935
[Epoch 19] train accuracy: 0.9145, loss: 0.2165
[Epoch 19] eval accuracy: 0.8755, loss: 0.3391
[Epoch 20] train accuracy: 0.9015, loss: 0.2574
[Epoch 20] eval accuracy: 0.8911, loss: 0.2771
[Epoch 21] train accuracy: 0.9178, loss: 0.2122
[Epoch 21] eval accuracy: 0.8838, loss: 0.3067
[Epoch 22] train accuracy: 0.9242, loss: 0.1886
[Epoch 22] eval accuracy: 0.8766, loss: 0.3010
[Epoch 23] train accuracy: 0.9278, loss: 0.1739
[Epoch 23] eval accuracy: 0.8869, loss: 0.3041
[Epoch 24] train accuracy: 0.9326, loss: 0.1684
[Epoch 24] eval accuracy: 0.8932, loss: 0.3034
[Epoch 25] train accuracy: 0.9363, loss: 0.1548
[Epoch 25] eval accuracy: 0.8620, loss: 0.3893
[Epoch 26] train accuracy: 0.9109, loss: 0.2554
[Epoch 26] eval accuracy: 0.8714, loss: 0.3310
[Epoch 27] train accuracy: 0.9293, loss: 0.1832
[Epoch 27] eval accuracy: 0.8797, loss: 0.3492
[Epoch 28] train accuracy: 0.9400, loss: 0.1560
[Epoch 28] eval accuracy: 0.8579, loss: 0.3690
[Epoch 29] train accuracy: 0.9478, loss: 0.1343
[Epoch 29] eval accuracy: 0.8952, loss: 0.3416
[Epoch 30] train accuracy: 0.9500, loss: 0.1241
[Epoch 30] eval accuracy: 0.8817, loss: 0.3586
[Epoch 31] train accuracy: 0.9568, loss: 0.1159
[Epoch 31] eval accuracy: 0.8786, loss: 0.3777
[Epoch 32] train accuracy: 0.9642, loss: 0.0955
[Epoch 32] eval accuracy: 0.8869, loss: 0.3891
[Epoch 33] train accuracy: 0.9627, loss: 0.0957
[Epoch 33] eval accuracy: 0.8859, loss: 0.3978
[Epoch 34] train accuracy: 0.9613, loss: 0.1012
[Epoch 34] eval accuracy: 0.8797, loss: 0.4356
[Epoch 35] train accuracy: 0.9639, loss: 0.0943
[Epoch 35] eval accuracy: 0.8911, loss: 0.4248
[Epoch 36] train accuracy: 0.9591, loss: 0.1076
[Epoch 36] eval accuracy: 0.8807, loss: 0.3640
[Epoch 37] train accuracy: 0.9680, loss: 0.0847
[Epoch 37] eval accuracy: 0.8817, loss: 0.4580
[Epoch 38] train accuracy: 0.9628, loss: 0.1094
[Epoch 38] eval accuracy: 0.8859, loss: 0.3399
[Epoch 39] train accuracy: 0.9683, loss: 0.0814
[Epoch 39] eval accuracy: 0.8932, loss: 0.4682 best
[Epoch 40] train accuracy: 0.9767, loss: 0.0621
[Epoch 40] eval accuracy: 0.8724, loss: 0.4918
[Epoch 41] train accuracy: 0.9766, loss: 0.0645
[Epoch 41] eval accuracy: 0.8745, loss: 0.5787
[Epoch 42] train accuracy: 0.9770, loss: 0.0567
[Epoch 42] eval accuracy: 0.8880, loss: 0.5924
[Epoch 43] train accuracy: 0.9773, loss: 0.0595
[Epoch 43] eval accuracy: 0.8921, loss: 0.4333
[Epoch 44] train accuracy: 0.9754, loss: 0.0749
[Epoch 44] eval accuracy: 0.8880, loss: 0.5451
[Epoch 45] train accuracy: 0.9839, loss: 0.0468
[Epoch 45] eval accuracy: 0.8807, loss: 0.5363
[Epoch 46] train accuracy: 0.9817, loss: 0.0545
[Epoch 46] eval accuracy: 0.8911, loss: 0.5097
[Epoch 47] train accuracy: 0.9844, loss: 0.0447
[Epoch 47] eval accuracy: 0.8817, loss: 0.5890
[Epoch 48] train accuracy: 0.9825, loss: 0.0497
[Epoch 48] eval accuracy: 0.8880, loss: 0.3969
[Epoch 49] train accuracy: 0.9807, loss: 0.0557
[Epoch 49] eval accuracy: 0.8776, loss: 0.5372
[Epoch 50] train accuracy: 0.9743, loss: 0.0827
[Epoch 50] eval accuracy: 0.8755, loss: 0.4848

## Category Training
[Epoch 1] train accuracy: 0.1983, loss: 2.0198
[Epoch 1] eval accuracy: 0.2448, loss: 1.7975
[Epoch 2] train accuracy: 0.3055, loss: 1.6805
[Epoch 2] eval accuracy: 0.3745, loss: 1.5395
[Epoch 3] train accuracy: 0.4214, loss: 1.4687
[Epoch 3] eval accuracy: 0.4616, loss: 1.3630
[Epoch 4] train accuracy: 0.4881, loss: 1.3303
[Epoch 4] eval accuracy: 0.5083, loss: 1.2581
[Epoch 5] train accuracy: 0.5290, loss: 1.2386
[Epoch 5] eval accuracy: 0.5249, loss: 1.1970
[Epoch 6] train accuracy: 0.5525, loss: 1.1746
[Epoch 6] eval accuracy: 0.5664, loss: 1.1300
[Epoch 7] train accuracy: 0.5929, loss: 1.0897
[Epoch 7] eval accuracy: 0.5923, loss: 1.0603
[Epoch 8] train accuracy: 0.6135, loss: 1.0300
[Epoch 8] eval accuracy: 0.5871, loss: 1.0596
[Epoch 9] train accuracy: 0.6409, loss: 0.9474
[Epoch 9] eval accuracy: 0.6131, loss: 1.0357
[Epoch 10] train accuracy: 0.6638, loss: 0.9051
[Epoch 10] eval accuracy: 0.6317, loss: 0.9732
[Epoch 11] train accuracy: 0.6905, loss: 0.8504
[Epoch 11] eval accuracy: 0.6411, loss: 0.9251
[Epoch 12] train accuracy: 0.7008, loss: 0.8120
[Epoch 12] eval accuracy: 0.6328, loss: 1.0101
[Epoch 13] train accuracy: 0.7195, loss: 0.7636
[Epoch 13] eval accuracy: 0.6504, loss: 0.9300
[Epoch 14] train accuracy: 0.7419, loss: 0.6930
[Epoch 14] eval accuracy: 0.6556, loss: 0.9673
[Epoch 15] train accuracy: 0.7584, loss: 0.6564
[Epoch 15] eval accuracy: 0.6390, loss: 1.0311
[Epoch 16] train accuracy: 0.7678, loss: 0.6282
[Epoch 16] eval accuracy: 0.6390, loss: 1.0116
[Epoch 17] train accuracy: 0.7877, loss: 0.5831
[Epoch 17] eval accuracy: 0.6463, loss: 1.0333
[Epoch 18] train accuracy: 0.8034, loss: 0.5299
[Epoch 18] eval accuracy: 0.6442, loss: 1.0656
[Epoch 19] train accuracy: 0.8210, loss: 0.4928
[Epoch 19] eval accuracy: 0.6411, loss: 1.0297
[Epoch 20] train accuracy: 0.8283, loss: 0.4740
[Epoch 20] eval accuracy: 0.6421, loss: 1.0434
[Epoch 21] train accuracy: 0.8317, loss: 0.4689
[Epoch 21] eval accuracy: 0.6452, loss: 1.0944
[Epoch 22] train accuracy: 0.8584, loss: 0.3975
[Epoch 22] eval accuracy: 0.6587, loss: 1.0373
[Epoch 23] train accuracy: 0.8724, loss: 0.3661
[Epoch 23] eval accuracy: 0.6255, loss: 1.0849
[Epoch 24] train accuracy: 0.8704, loss: 0.3569
[Epoch 24] eval accuracy: 0.6338, loss: 1.1418
[Epoch 25] train accuracy: 0.8777, loss: 0.3395
[Epoch 25] eval accuracy: 0.6546, loss: 1.1584
[Epoch 26] train accuracy: 0.8991, loss: 0.2956
[Epoch 26] eval accuracy: 0.6691, loss: 1.0566
[Epoch 27] train accuracy: 0.8961, loss: 0.2974
[Epoch 27] eval accuracy: 0.6753, loss: 1.0716
[Epoch 28] train accuracy: 0.9078, loss: 0.2763
[Epoch 28] eval accuracy: 0.6556, loss: 1.1292
[Epoch 29] train accuracy: 0.9023, loss: 0.2845
[Epoch 29] eval accuracy: 0.6691, loss: 1.1255
[Epoch 30] train accuracy: 0.9082, loss: 0.2717
[Epoch 30] eval accuracy: 0.6515, loss: 1.1155
[Epoch 31] train accuracy: 0.9149, loss: 0.2595
[Epoch 31] eval accuracy: 0.6452, loss: 1.1099
[Epoch 32] train accuracy: 0.9092, loss: 0.2662
[Epoch 32] eval accuracy: 0.6732, loss: 1.1552
[Epoch 33] train accuracy: 0.9262, loss: 0.2209
[Epoch 33] eval accuracy: 0.6701, loss: 1.1038
[Epoch 34] train accuracy: 0.9374, loss: 0.1864
[Epoch 34] eval accuracy: 0.6701, loss: 1.1785
[Epoch 35] train accuracy: 0.9298, loss: 0.2097
[Epoch 35] eval accuracy: 0.6255, loss: 1.3873
[Epoch 36] train accuracy: 0.9278, loss: 0.2145
[Epoch 36] eval accuracy: 0.6649, loss: 1.1391
[Epoch 37] train accuracy: 0.9297, loss: 0.2046
[Epoch 37] eval accuracy: 0.6369, loss: 1.3661
[Epoch 38] train accuracy: 0.9414, loss: 0.1823
[Epoch 38] eval accuracy: 0.6763, loss: 1.1852 best
[Epoch 39] train accuracy: 0.9310, loss: 0.2028
[Epoch 39] eval accuracy: 0.6732, loss: 1.1910
[Epoch 40] train accuracy: 0.9429, loss: 0.1796
[Epoch 40] eval accuracy: 0.6598, loss: 1.1750
[Epoch 41] train accuracy: 0.9459, loss: 0.1620
[Epoch 41] eval accuracy: 0.6535, loss: 1.2684
[Epoch 42] train accuracy: 0.9465, loss: 0.1568
[Epoch 42] eval accuracy: 0.6639, loss: 1.2489
[Epoch 43] train accuracy: 0.9420, loss: 0.1727
[Epoch 43] eval accuracy: 0.6546, loss: 1.2215
[Epoch 44] train accuracy: 0.9557, loss: 0.1311
[Epoch 44] eval accuracy: 0.6878, loss: 1.2511
[Epoch 45] train accuracy: 0.9550, loss: 0.1351
[Epoch 45] eval accuracy: 0.6504, loss: 1.4122
[Epoch 46] train accuracy: 0.9550, loss: 0.1527
[Epoch 46] eval accuracy: 0.6577, loss: 1.3268
[Epoch 47] train accuracy: 0.9590, loss: 0.1315
[Epoch 47] eval accuracy: 0.6390, loss: 1.4409
[Epoch 48] train accuracy: 0.9496, loss: 0.1696
[Epoch 48] eval accuracy: 0.6483, loss: 1.2056
[Epoch 49] train accuracy: 0.9571, loss: 0.1316
[Epoch 49] eval accuracy: 0.6608, loss: 1.3016
[Epoch 50] train accuracy: 0.9515, loss: 0.1526
[Epoch 50] eval accuracy: 0.6494, loss: 1.3501