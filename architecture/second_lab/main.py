import sys

lenvalue = 0
lenExpo = 0
lenMantissa = 0
expi = 0
max_value_expo = 0
min_value_expo = 0
zero = ''
inf = ''
pos_nan_ans = ''
positive_zero = ''
negative_zero = ''
positive_inf = ''
negative_inf = ''
pos_min_value = ''
neg_min_value = ''
positive_max_value = ''
negative_max_value = ''
neg_nan_ans = ''


def create_consts(type):
    global lenvalue, lenExpo, lenMantissa, expi, max_value_expo, min_value_expo, zero, inf, pos_nan_ans, neg_nan_ans, positive_zero, negative_zero, positive_inf, negative_inf, pos_min_value, neg_min_value, positive_max_value, negative_max_value
    if type == "f":
        lenvalue = 32
        lenExpo = 8
        lenMantissa = 23
        expi = 127
    else:
        lenvalue = 16
        lenExpo = 5
        lenMantissa = 10
        expi = 15
    max_value_expo = expi
    min_value_expo = (-expi) + 1
    neg_nan_ans = "1" * lenvalue  # for ans
    pos_nan_ans = "0" + "1" * (lenvalue - 1)  # for ans
    zero = '0'.zfill(lenvalue - 1)
    positive_zero = '0' + zero
    negative_zero = '1' + zero
    inf = '1' * lenExpo + '0' * lenMantissa
    positive_inf = '0' + inf
    negative_inf = '1' + inf
    pos_min_value = '0' + ('0' * (lenvalue - 2) + '1')
    neg_min_value = '1' + ('0' * (lenvalue - 2) + '1')
    positive_max_value = '0' + ('1' * (lenExpo - 1) + '0' + '1' * lenMantissa)
    negative_max_value = '1' + ('1' * (lenExpo - 1) + '0' + '1' * lenMantissa)


def create_float(num, type):
    create_consts(type)
    binary = (num.zfill(lenvalue))[-lenvalue:]
    sign = int(binary[0])
    is_nan = check_nan(binary)
    is_inf = check_inf(binary)
    is_zero = check_zero(binary)
    is_norm = False
    num = binary[-lenMantissa:]

    if is_denorm(binary):
        exp = min_value_expo - num.index('1') - 1
        man_val = int(num, 2)
    else:
        is_norm = True
        exp = binary[1:1 + lenExpo]
        exp = int(exp, 2) - expi
        man_val = int("1" + num, 2)

    return {
        'binary': binary,
        'sign': sign,
        'is_nan': is_nan,
        'is_inf': is_inf,
        'is_zero': is_zero,
        'is_norm': is_norm,
        'exp': exp,
        'man_val': man_val
    }


def bintohex1(value):
    value = int(value, 2)
    value = hex(value)[2:]
    if type == 'h':
        return value.zfill(4).upper()
    return value.zfill(8).upper()


def bintohex2(q):
    res = ''
    for i in range(0, len(q), 4):
        res += hex(int(q[i:i + 4], 2))[2:]
    return res


def float_to_str(value, type, is_operation_result):
    create_consts(type)
    if is_unic(value):
        if type == 'h':
            ans = "0x" + bintohex1(value['binary'])
        else:
            ans = "0x" + bintohex1(value['binary'])

        if value['is_zero']:
            if type == "h":
                if value['sign'] == 1:
                    return "-0x0.000p+0" + " " + ans
                return "0x0.000p+0" + " " + ans
            else:
                if value['sing'] == 1:
                    return "-0x0.000000p+0" + ans
                return "0x0.000000p+0" + ans
        elif value['is_inf']:
            if value['sign'] == 1:
                return "-inf" + " " + ans
            return "inf" + " " + ans
        elif value['is_nan']:
            if is_operation_result:
                if type == 'h':
                    ans = "0xFE00"
                else:
                    ans = "0xFFC00000"
                return "nan" + " " + ans
            else:
                return "nan" + " " + ans

    else:
        if type == 'h':
            ans = "0x" + bintohex1(value['binary'])
        else:
            ans = "0x" + bintohex1(value['binary'])
        if value['sign'] == 1:
            res = "-0x1."
        else:
            res = "0x1."
        bin_man_val = bin(value['man_val'])[3:]
        ln = len(bin_man_val)
        if type == "h":
            bin_res = bin_man_val + "0".zfill(12 - ln)
        else:
            bin_res = bin_man_val + "0".zfill(24 - ln)
        res += bintohex2(bin_res)
        if value['exp'] >= 0:
            return res + "p+" + str(value['exp']) + " " + ans
        return res + "p" + str(value['exp']) + " " + ans


def add_float(float1, float2, type, toward_typo):
    create_consts(type)
    diff = float1['exp'] - float2['exp']
    if diff < 0 or (float1['man_val'] < float2[
        'man_val'] and diff == 0):
        return add_float(float2, float1, type,
                         toward_typo)

    if is_unic(float1) or is_unic(float2):
        if float1['is_inf'] or float2['is_inf']:
            if float1['is_inf'] and float2['is_inf']:
                if float1['sign'] == float2['sign']:
                    return float1
                else:
                    return create_float(pos_nan_ans, type)
        if float1['is_zero'] or float2['is_zero']:
            if float1['is_zero']:
                return float2
            else:
                return float1

    if float2['binary'][1:] == float1['binary'][1:] and float1['sign'] != float2['sign']:
        return create_float(positive_zero, type)
    if not float1['is_norm']:
        float1['man_val'] = getNormalMantiss(float1['man_val'])
    if not float2['is_norm']:
        float2['man_val'] = getNormalMantiss(float2['man_val'])
    resman = float1['man_val'] * 2 ** diff
    resMantissa = bin(resman)[2:]
    if float2['sign'] == float1['sign']:
        resman += float2['man_val']
    else:
        resman -= float2['man_val']
    man = getNormalMantiss(resman)
    exp = float1['exp'] + len(bin(resman)[2:]) - len(resMantissa)
    return check(float1['sign'], exp, man, bin(man)[2:], type, toward_typo)


def mul_float(float1, float2, type, toward_typo):
    create_consts(type)
    if is_unic(float1) or is_unic(float2):
        if (float1['is_inf'] and float2['is_zero']) or (
                float1['is_zero'] and float2['is_inf']):
            if float1['sign'] == float2['sign']:
                return create_float(pos_nan_ans, type)
            else:
                return create_float(neg_nan_ans, type)
        if float1['is_inf'] or float2['is_inf']:
            if float1['is_inf']:
                if float1['sign'] == float2['sign']:
                    float1['sign'] = 0
                    float1['binary'] = positive_inf
                else:
                    float1['sign'] = 1
                    float1['binary'] = negative_inf
                return float1
            else:
                if float1['sign'] == float2['sign']:
                    float2['sign'] = 0
                    float2['binary'] = positive_inf
                else:
                    float2['sign'] = 1
                    float2['binary'] = negative_inf
                return float2
        if float1['is_zero'] or float2['is_zero']:
            if float1['sign'] == float2['sign']:
                curr_sign = 0
            else:
                curr_sign = 1
            if float1['is_zero']:
                float1['sign'] = curr_sign
                return float1
            else:
                float2['sign'] = curr_sign
                return float2
    sgn = 0 if float1['sign'] == float2['sign'] else 1
    man = float1['man_val'] * float2['man_val']
    bin_mant = bin(man)[2:]
    exp = float1['exp'] + float2['exp']
    if not float1['is_norm']:
        exp += len(bin_mant) - 2 * lenMantissa - 1 + (min_value_expo - float1['exp'])
    elif not float2['is_norm']:
        exp += len(bin_mant) - 2 * lenMantissa - 1 + (min_value_expo - float2['exp'])
    else:
        exp += len(bin_mant) - 2 * lenMantissa - 1
    return check(sgn, exp, man, bin_mant, type, toward_typo)


def div_float(float1, float2, type, toward_typo):
    create_consts(type)
    if is_unic(float1) or is_unic(float2):
        sgn = 0 if float1['sign'] == float2['sign'] else 1
        if float1['is_zero'] or float2['is_zero']:
            if float1['is_zero'] and float2['is_zero']:
                if sgn == 0:
                    return create_float(pos_nan_ans, type)
                else:
                    return create_float(neg_nan_ans, type)
            else:
                if float1['is_zero']:
                    return sgn_type(sgn, negative_zero, positive_zero, type)
                else:
                    return sgn_type(sgn, negative_inf, positive_inf, type)
        if float2['is_inf'] and float1['is_inf']:
            if sgn == 0:
                return create_float(pos_nan_ans, type)
            else:
                return create_float(neg_nan_ans, type)
        if float2['is_inf']:
            return sgn_type(sgn, negative_zero, positive_zero, type)

        if float1['is_inf']:
            return sgn_type(sgn, negative_inf, positive_inf, type)
    f1, f2 = 0, 0
    if not float1['is_norm']:
        f1 = 1
    if not float2['is_norm']:
        f2 = 1
    sgn = 0 if float1['sign'] == float2['sign'] else 1
    times = 0
    hard_times = 0
    superf = 0
    if float1['man_val'] < float2['man_val']:
        float1['exp'] -= 1
        float1['man_val'] *= 2
        hard_times += 1

    while float1['man_val'] > float2['man_val'] * 2:
        float2['man_val'] *= 2
        float2['exp'] -= 1
        times += 1
    if f1 and f1 != f2:
        if float1['exp'] > 0:
            float1['exp'] -= times
        else:
            float1['exp'] += times
    if f2 and f1 != f2:
        if float2['exp'] > 0:
            float2['exp'] -= times
        else:
            float2['exp'] += times
    exp = float1['exp'] - float2['exp']
    if (f1 or f2) and times == 0 and hard_times == 0:
        exp -= 1
    diff = 2 ** (lenMantissa * 2)
    man = (float1['man_val'] * diff) // float2['man_val']
    return check(sgn, exp, man, bin(man)[2:], type, toward_typo)


def check(sgn, exp, man, bin_mant, type, toward_typo):
    create_consts(type)
    if exp < min_value_expo - lenMantissa:
        if toward_typo == 0:
            return sgn_type(sgn, negative_zero, positive_zero, type)
        if toward_typo == 1:
            return sgn_type(sgn, negative_zero, positive_zero, type)
        if toward_typo == 2:
            return sgn_type(sgn, negative_zero, pos_min_value, type)
        if toward_typo == 3:
            return sgn_type(sgn, neg_min_value, positive_zero, type)
    if exp > max_value_expo:
        if toward_typo == 0:
            return sgn_type(sgn, negative_max_value, positive_max_value, type)
        if toward_typo == 1:
            return sgn_type(sgn, negative_inf, positive_inf, type)
        if toward_typo == 2:
            return sgn_type(sgn, negative_max_value, positive_inf, type)
        if toward_typo == 3:
            return sgn_type(sgn, negative_inf, positive_max_value, type)
    if exp < min_value_expo:
        man += 2 ** (len(bin_mant) - (expi + exp))
        bin_mant, flag = do_toward(bin(man)[3:], sgn, toward_typo)
        exp = '0' * (lenExpo - 1)
        if flag == 1:
            sgn = str(sgn)
            exp = exp + "1"
            man = "0".zfill(lenMantissa)
            return create_float(sgn + exp + man, type)
        sgn = str(sgn)
        exp = "0" + exp
        man = bin_mant.zfill(lenMantissa)
        return create_float(sgn + exp + man, type)
    bin_mant, flag = do_toward(bin_mant[1:], sgn, toward_typo)
    if flag == 1 and exp == max_value_expo:
        return sgn_type(sgn, negative_inf, positive_inf, type)
    elif flag == -1 and exp == min_value_expo:
        sgn = str(sgn)
        exp = "0".zfill(lenExpo)
        man = "0".zfill(lenMantissa - 1) + "1"
        return create_float(sgn + exp + man, type)
    else:
        bin_exp = (bin(exp + expi + flag)[2:]).zfill(lenExpo)
        sgn = str(sgn)
        return create_float(sgn + bin_exp + bin_mant, type)


def opposite(float_num, type):
    create_consts(type)
    if float_num['sign'] == 1:
        return create_float('0' + float_num['binary'][1:], type)
    else:
        return create_float('1' + float_num['binary'][1:], type)


def do_toward(b, sign, toward_typo):
    create_consts(type)
    cnt = 0
    for i in range(lenMantissa, len(b)):
        if b[i] == '1':
            cnt += 1
    if cnt == 0 or toward_typo == 0:
        return b[:lenMantissa], 0
    if toward_typo == 1:
        if b[lenMantissa] == '0':
            return superround(b, 1)
        elif '1' in b[lenMantissa + 1:]:
            return superround(b, 0)
        elif b[lenMantissa - 1] == '0':
            return superround(b, 1)
        else:
            return superround(b, 0)

    if toward_typo == 2 or toward_typo == 3:
        if (toward_typo == 2 and sign) or (toward_typo == 3 and not sign):
            return superround(b, 1)
        return superround(b, 0)


def sgn_type(sgn, true, false, type):
    create_consts(type)
    if sgn:
        return create_float(true, type)
    return create_float(false, type)


def getNormalMantiss(val):
    create_consts(type)
    while val < 2 ** lenMantissa:
        val *= 2
    return val


def superround(binM, flag):
    create_consts(type)
    if flag == 0:
        if '1' not in binM[lenMantissa:]:
            return binM[:lenMantissa], 0
        elif '0' not in binM[:lenMantissa]:
            num = "0".zfill(lenMantissa)
            return num, 1
        else:
            num = int(binM[:lenMantissa], 2) + 1
            binN = bin(num)[2:].zfill(lenMantissa)
            return binN, 0
    else:
        if '1' in binM[lenMantissa:]:
            return binM[:lenMantissa], 0
        elif '1' not in binM[:lenMantissa]:
            num = "1".zfill(lenMantissa)
            return num, -1
        else:
            num = int(binM[:lenMantissa], 2) - 1
            res = bin(num)[2:].zfill(lenMantissa)
            return res, 0


def check_zero(num):
    create_consts(type)
    return num[1:] == zero


def is_denorm(num):
    create_consts(type)
    return (not (num[1:] == zero)) and num[1:1 + lenExpo] == '0' * lenExpo


def check_inf(num):
    create_consts(type)
    return num[1:] == inf


def check_nan(num):
    create_consts(type)
    return (not (num[1:] == inf)) and num[1:1 + lenExpo] == '1' * lenExpo


def is_unic(value):
    return value['is_zero'] or value['is_inf'] or value['is_nan']


def main():
    try:
        global type
        f = 0
        args = [s.lower() for s in sys.argv[1:]]
        type = args[0]
        roun = int(args[1])
        if len(args) == 3:
            n1 = create_float(bin(int(args[2], 16))[2:], type)
            result = n1
        else:
            n1 = create_float(bin(int(args[2], 16))[2:], type)
            n2 = create_float(bin(int(args[4], 16))[2:], type)
            oper = args[3]
            if n1['is_nan']:
                if n1['sign'] == n2['sign'] and (oper == '/' or oper == '*'):
                    n1['binary'] = '0' + n1['binary'][1:]
                elif n1['sign'] != n2['sign'] and (oper == '/' or oper == '*'):
                    n1['binary'] = '1' + n1['binary'][1:]
                result = n1
            elif n2['is_nan']:
                if n1['sign'] == n2['sign'] and (oper == '/' or oper == '*'):
                    n2['binary'] = '0' + n2['binary'][1:]
                elif n1['sign'] != n2['sign'] and (oper == '/' or oper == '*'):
                    n2['binary'] = '1' + n2['binary'][1:]
                result = n2
            elif oper == '+':
                result = add_float(n1, n2, type, roun)
                f = 1
            elif oper == '-':
                result = add_float(n1, opposite(n2, type), type, roun)
                f = 1
            elif oper == '*':
                result = mul_float(n1, n2, type, roun)
                f = 1
            elif oper == '/':
                result = div_float(n1, n2, type, roun)
                f = 1
        print(float_to_str(result, type, f))
    except MemoryError:
        print(file=sys.stderr)
    except ValueError as e:
        print(file=sys.stderr)
    except FileNotFoundError:
        print(file=sys.stderr)
    except Exception as e:
        print(file=sys.stderr)


if __name__ == "__main__":
    main()
