#include "hit.h"

bool hit_test(float x, float y, float z) {
    float x4 = x * x * x * x;
    float x3 = x * x * x;
    float y2 = y * y;
    float z2 = z * z;
    return (x4 - 2 * x3) + 4 * (y2 + z2) <= 0;
}

const float* get_axis_range() {
    static float axis_range[6];

    axis_range[0] = 0.0f;    //  x
    axis_range[1] = 2.0f;
    axis_range[2] = -0.65f;  //  y
    axis_range[3] = 0.65f;
    axis_range[4] = -0.65f;  // z
    axis_range[5] = 0.65f;

    return axis_range;
}
   
