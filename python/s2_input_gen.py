import random
import sys

# FORMATO INPUT
#V0 [10000, 100000]
#x y carga
# ...
#x y carga


D = 10 ** -8
N = 16 ** 2
N_SIDE = 16
M = 10 ** -27
V0_MIN = 10000
V0_MAX = 100000
Y_OFFSET_MIN = -1
Y_OFFSET_MAX = 1


def get_charge(x, y):
    return 1 if (x + y) % 2 == 0 else -1


def parse_args():
    total = len(sys.argv)
    if total > 3:
        print("""
        Only 2 arguments are supported: 
        1. specifies V0 (blank for random between [{}, {}]
        2. specifies yOffset multiplier for D (blank for random between [{}, {}]
        """.format(V0_MIN, V0_MAX, Y_OFFSET_MIN, Y_OFFSET_MAX))
        quit()
    if total == 3:
        v0 = int(sys.argv[1])
        if not (V0_MIN <= v0 <= V0_MAX):
            print('v0 is too small or too large')
            quit()

        y_offset = float(sys.argv[2])
        if not (Y_OFFSET_MIN <= y_offset <= Y_OFFSET_MAX):
            print('y_offset is too small or too large')
            quit()

        return v0, y_offset * D

    random.seed()
    return random.uniform(V0_MIN, V0_MAX), random.uniform(Y_OFFSET_MIN, Y_OFFSET_MAX) * D


def generate(file_path, velocity, y_offset):
    f = open(file_path, "w")
    f.write(str(velocity))
    f.write('\n')
    f.write(str(y_offset))
    f.write('\n')

    for y in range(N_SIDE):
        for x in range(N_SIDE):
            # x
            f.write(str(x))
            f.write('\t')

            # y
            f.write(str(y))
            f.write('\t')

            # charge
            f.write(str(get_charge(x, y)))

            f.write('\n')

    f.close()


def main():
    velocity, y_offset = parse_args()
    file_path = '../input.txt'
    generate(file_path, velocity, y_offset)
    

if __name__ == '__main__':
    main()

