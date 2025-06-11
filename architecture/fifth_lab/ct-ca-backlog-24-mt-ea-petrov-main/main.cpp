#pragma once
#include <random>
#include "hit.h"
#include "xor_shift_random.h"
#include <omp.h>
#include <iostream>
#include <fstream>
#include <string>
#include <cstdlib>

using namespace std;

long long realization1(const long long iterations) {
    const float *axis_range = new float[6];
    axis_range = get_axis_range();
    uint32_t seed = random_device{}();
    xor_shift_random random(seed);
    long long inside = 0;
    for (long long i = 0; i < iterations; i++) {
        float x, y, z;
        if (i % 3 == 0) {
            x = random.generate(axis_range[0], axis_range[1]);
        }
        if (i % 3 == 1) {
            y = random.generate(axis_range[2], axis_range[3]);
        }
        if (i % 3 == 2) {
            z = random.generate(axis_range[4], axis_range[5]);
        }
        if (hit_test(x, y, z)) {
            inside++;
        }
    }
    return inside;
}

long long dynamic2(unsigned long long iterations, const int threads, const int chunk_size) {
    const float *axis_range = new float[6];
    axis_range = get_axis_range();

    long long result = 0;
#pragma omp parallel num_threads(threads)
    {
        uint32_t seed = random_device{}() + omp_get_thread_num();;
        xor_shift_random random(seed);

        long long inside = 0;
#pragma omp for schedule(dynamic, chunk_size)
        for (long long i = 0; i < iterations; i++) {
            float x, y, z;
            if (i % 3 == 0) {
                x = random.generate(axis_range[0], axis_range[1]);
            }
            if (i % 3 == 1) {
                y = random.generate(axis_range[2], axis_range[3]);
            }
            if (i % 3 == 2) {
                z = random.generate(axis_range[4], axis_range[5]);
            }
            if (hit_test(x, y, z)) {
                inside++;
            }
        }

#pragma omp atomic
        result += inside;
    }

    return result;
}

long long static2(long long iterations, const int threads, const int chunk_size) {

    const float *axis_range = new float[6];
    axis_range = get_axis_range();

    long long result = 0;
    uint32_t seed = random_device{}();

#pragma omp parallel num_threads(threads)
    {
        long long inside = 0;

        uint32_t seed = random_device{}() + omp_get_thread_num();
        xor_shift_random random(seed);

#pragma omp for schedule(static, chunk_size)
        for (long long i = 0; i < iterations; i++) {
            float x, y, z;
            if (i % 3 == 0) {
                x = random.generate(axis_range[0], axis_range[1]);
            }
            if (i % 3 == 1) {
                y = random.generate(axis_range[2], axis_range[3]);
            }
            if (i % 3 == 2) {
                z = random.generate(axis_range[4], axis_range[5]);
            }
            if (hit_test(x, y, z)) {
                inside++;
            }
        }

#pragma omp atomic
        result += inside;
    }

    return result;
}

long long static3(const long long iterations, const int threads, const int chunk_size) {
    const float *axis_range = get_axis_range();
    long long result = 0;

#pragma omp parallel num_threads(threads)
    {
        int thread_id = omp_get_thread_num();
        xor_shift_random random(random_device{}() + thread_id);
        long long inside = 0;


        long long left = thread_id * chunk_size;
        long long right;
        if (thread_id != threads - 1) {
            right = left + chunk_size;
        } else {
            right = iterations;
        }

        for (long long i = left; i < right; i++) {
            float x, y, z;
            if (i % 3 == 0) {
                x = random.generate(axis_range[0], axis_range[1]);
            }
            if (i % 3 == 1) {
                y = random.generate(axis_range[2], axis_range[3]);
            }
            if (i % 3 == 2) {
                z = random.generate(axis_range[4], axis_range[5]);
            }

            if (hit_test(x, y, z)) {
                inside++;
            }
        }

#pragma omp atomic
        result += inside;
    }

    return result;
}

long long dynamic3(const long long iterations, const int threads, const int chunk_size) {
    const float* axis_range = get_axis_range();
    long long result = 0;
    long long next_iteration = 0;

#pragma omp parallel num_threads(threads)
    {
        xor_shift_random random(random_device{}() + omp_get_thread_num());
        long long inside = 0;

        while (true) {
            long long left = 0;

#pragma omp critical
            {
                left = next_iteration;
                next_iteration += chunk_size;
            }

            if (left >= iterations) {
                break;
            }

            long long right = left + chunk_size;
            right = min(right, iterations);
            for (long long i = left; i < right; i++) {
                float x, y, z;
                if (i % 3 == 0) {
                    x = random.generate(axis_range[0], axis_range[1]);
                }
                if (i % 3 == 1) {
                    y = random.generate(axis_range[2], axis_range[3]);
                }
                if (i % 3 == 2) {
                    z = random.generate(axis_range[4], axis_range[5]);
                }

                if (hit_test(x, y, z)) {
                    inside++;
                }
            }
        }

#pragma omp atomic
        result += inside;
    }

    return result;
}


float count(const long long total, const long long inside) {
    const float *axis_range = new float[6];
    axis_range = get_axis_range();
    float all = (axis_range[1] - axis_range[0]) * (axis_range[3] - axis_range[2]) * (axis_range[5] - axis_range[4]);
    return all * inside / total;
}


int main(int len, char *arguments[]) {

    string input_file = "";
    string output_file = "";
    int realization = 0;
    int threads = 0;
    string kind = "static";
    int chunk_size = 0;

    for (int i = 1; i < len; i++) {
        string arg = arguments[i];
        if (arg == "--input") {
            if (i < len - 1) {
                i++;
                input_file = arguments[i];
            }
        } else if (arg == "--output") {
            if (i < len - 1) {
                i++;
                output_file = arguments[i];
            }
        } else if (arg == "--realization") {
            if (i < len - 1) {
                i++;
                try {
                    realization = stoi(arguments[i]);
                } catch (const invalid_argument &e) {
                    cerr << "Error in arguments" << endl;
                    exit(EXIT_FAILURE);
                }
            }
        } else if (arg == "--kind") {
            if (i < len - 1) {
                i++;
                kind = arguments[i];
            }
        } else if (arg == "--chunk_size") {
            if (i < len - 1) {
                i++;
                try {
                    chunk_size = stoi(arguments[i]);
                } catch (const invalid_argument &e) {
                    cerr << "Error in arguments" << endl;
                    exit(EXIT_FAILURE);
                }
            }
        } else if (arg == "--threads") {
            if (i < len - 1) {
                i++;
                try {
                    threads = stoi(arguments[i]);
                } catch (const invalid_argument &e) {
                    cerr << "Error in arguments" << endl;
                    exit(EXIT_FAILURE);
                }
            }
        } else {
            cerr << "Unknown argument: " << arg << endl;
            exit(EXIT_FAILURE);
        }
    }

    if ((realization != 1 && realization != 2 && realization != 3) ||
        input_file == "" || output_file == "" || threads < 0 ||
        (kind != "static" && kind != "dynamic") || chunk_size < 0) {
        cerr << "Error in arguments" << endl;
        exit(EXIT_FAILURE);
    }
    try {
        long long iterations;
        ifstream infile(input_file);
        infile >> iterations;
        double start, end, res = 0;
        if (realization == 1) {
            threads = 0;
            start = omp_get_wtime();
            res = count(iterations, realization1(iterations));
            end = omp_get_wtime();
        }
        if (realization == 2) {
            if (threads == 0) {
                threads = omp_get_max_threads();
            }
            if (chunk_size == 0) {
                if (kind == "dynamic") {
                    chunk_size = iterations / threads;
                    //cout << chunk_size;
                }
                if (kind == "static")
                    chunk_size = 1000;
            }
            if (kind == "static") {
                start = omp_get_wtime();
                res = count(iterations, static2(iterations, threads, chunk_size));
                end = omp_get_wtime();
            } else if (kind == "dynamic") {
                start = omp_get_wtime();
                res = count(iterations, dynamic2(iterations, threads, chunk_size));
                end = omp_get_wtime();
            }
        }

        if (realization == 3) {
            if (threads == 0) {
                threads = omp_get_max_threads();
            }
            if (chunk_size == 0) {
                chunk_size = iterations / threads;
            }
            if (kind == "static") {
                start = omp_get_wtime();
                res = count(iterations, static3(iterations, threads, chunk_size));
                end = omp_get_wtime();
            } else if (kind == "dynamic") {
                start = omp_get_wtime();
                res = count(iterations, dynamic3(iterations, threads, chunk_size));
                end = omp_get_wtime();
            }
        }

        ofstream file(output_file, ios::app);
        file << res << endl;
        //cout << res << endl;
        cout << "Time (" << threads << " thread(s)): " << (end - start) * 1000 << " ms" << std::endl;
        return 0;

    } catch (const runtime_error &e) {
        cerr << "Error with file";
        exit(EXIT_FAILURE);
    }
}
