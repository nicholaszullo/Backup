# Part B

## 1st conv layer
The categorical network contains much smoother patterns than the domain. Some kernels in categorical are long connections of similar color. In domain, the patterns are more jagged. Some smooth sections exist, but are much less frequent than in the categorical model.

## 2nd conv layer
This layer is similar between both categorical and domain models, in the sense that some smooth patterns exist, but are not frequent. The models do not contain similar colors at each kernel, but similar streaks of colors exist in different kernels. 

## 3rd, 4th, and 5th Conv Layer
Similar patterns exist in both models. The images are only 3x3, so it is easy for similar patterns to appear. The kernels in each model are not the same at each kernel.

## Overall
I do not expect these kernels do be very similar because they are learning completely different tasks. They are extracting features that are not shared between them. Classifying objects in an image is much different than classifying the type of domain used to create the picture. 