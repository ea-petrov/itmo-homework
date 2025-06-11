import sys


def check(value):
    if a + b <= 32 and a != 32:  # a = 32 b = 0
        return value & ((1 << (a + b)) - 1)
    res = abs(value) & ((1 << a) - 1)
    if value < 0:
        return -res
    return res


def invert(s):
    return ''.join(['1', '0'][int(i)] for i in s)


def get_parts(value):
    sign = (value >> (a + b - 1)) & 1
    bits = check(value)

    if sign == 0:
        int_part = bits >> b
        frac_part = bits & ((1 << b) - 1)
    else:
        abs_val = (1 << (a + b)) - bits
        int_part = -1 * (abs_val >> b)
        frac_part = abs_val & ((1 << b) - 1)

    return int_part, frac_part


def get_answer(value):
    int_part, frac_part = get_parts(value)
    sign = (value >> (a + b - 1)) & 1
    integer, frac = get_res(int_part, frac_part)
    if sign:
        if integer[0] != "-":
            return "-" + integer + "." + frac
    return integer + "." + frac


def get_res(int_part, frac_part):
    frac = (frac_part * 1000) // (1 << b)
    rem = (frac_part * 1000) % (1 << b)
    sign = (int_part >> (a + b - 1)) & 1

    if toward == 0:
        return str(int_part), str(frac).zfill(3)
    elif toward == 1:
        if rem * 2 > (1 << b) or (rem * 2 == (1 << b) and frac % 2 != 0):
            frac += 1
    elif toward == 2:
        if sign == 0 and rem != 0:
            frac += 1
    elif toward == 3:
        if sign == 1 and rem != 0:
            frac += 1

    return str(int_part), str(frac).zfill(3)


def multi(val1, val2):
    if val1 == 0 or val2 == 0:
        return 0
    sign1 = (val1 >> (a + b - 1)) & 1
    sign2 = (val2 >> (a + b - 1)) & 1
    value1 = check(val1) if sign1 == 0 else (1 << (a + b)) - check(val1)
    value2 = check(val2) if sign2 == 0 else (1 << (a + b)) - check(val2)
    value = value1 * value2
    mod = 1 << b
    result = do_toward(value, mod, 0 if sign1 == sign2 else 1)
    return result


def divide_values(num1, num2):
    sign1 = (num1 >> (a + b - 1)) & 1
    sign2 = (num2 >> (a + b - 1)) & 1
    value1 = check(num1) if sign1 == 0 else (1 << (a + b)) - check(num1)
    value2 = check(num2) if sign2 == 0 else (1 << (a + b)) - check(num2)
    if value2 == 0:
        return 0
    value = value1 * (1 << b)
    mod = value2
    result = do_toward(value, mod, 0 if sign1 == sign2 else 1)
    return result


def do_toward(value, mod, is_negative):
    res = value // mod
    rem = value % mod
    if toward == 1:
        if rem * 2 > mod:
            res += 1
        elif rem * 2 < mod:
            res = res
        else:
            if res % 2 != 0:
                res += 1
    elif toward == 2:
        if rem != 0 and not is_negative:
            res += 1
    elif toward == 3:
        if rem != 0 and not is_negative:
            res -= 1
    if is_negative:
        res = - res
    else:
        res = res
    return res


def main(t):
    try:
        global a, b, toward
        w = t[0].split(".")
        a = int(w[0])
        b = int(w[1])
        toward = int(t[1])
        if len(t) == 5:
            n = t[2]
            m = t[4]
            n = n[2:]
            m = m[2:]
            n, m = int(n, 16), int(m, 16)
            if t[3] == "+":
                print(get_answer((n + m)))
            elif t[3] == "-":
                print(get_answer((n - m)))
            elif t[3] == "*":
                res = multi(n, m)
                if res == 0:
                    print("0.000")
                else:
                    print(get_answer(res))
            elif t[3] == "/":
                res = divide_values(n, m)
                if res == 0:
                    print("div_by_0")
                else:
                    print(get_answer(res))
        if len(t) == 3:
            n = int(t[2], 16)
            print(get_answer(n))
    except MemoryError:
        print(file=sys.stderr)
    except ValueError as e:
        print(file=sys.stderr)
    except FileNotFoundError:
        print(file=sys.stderr)
    except Exception as e:
        print(file=sys.stderr)


if __name__ == "__main__":
    args = sys.argv[1:]
    main(args)
