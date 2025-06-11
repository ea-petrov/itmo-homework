#pragma once

#include <cstdint>
#include <chrono>

#pragma once

#include <cstdint>
#include <chrono>

struct xor_shift_random {
    uint32_t seed_a, seed_b;

    xor_shift_random(uint32_t seed_a, uint32_t seed_b = 0x12345678)
            : seed_a(seed_a), seed_b(seed_b ^ seed_a) {
    }

    float generate(float min, float max) {
        uint32_t x = seed_a ^ (seed_a << 2);
        uint32_t y = x ^ (x >> 7);
        uint32_t b = (seed_b ^ (seed_b >> 3)) ^ y;

        seed_a = seed_b;
        seed_b = b;

        int num24 = int(b & 0xFFFFFF);
        float value = 0x1p-24f * float(num24);
        float result = min + (max - min) * value;
        return result;
    }
};
