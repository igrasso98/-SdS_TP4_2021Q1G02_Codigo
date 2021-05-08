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


def get_charge(x, y):
    return 1 if (x + y) % 2 == 0 else -1


def parse_args():
    total = len(sys.argv)
    if total > 2:
        print("Only 1 argument is supported: 1. specifies V0 (blank for random between [" + str(V0_MIN) + ", " + str(V0_MAX) + "]")
        quit()
    if total == 2:
        n = int(sys.argv[1])
        if not (V0_MIN <= n <= V0_MAX):
            print('v0 is too small or too large')
            quit()

        return n

    random.seed()
    return random.uniform(V0_MIN, V0_MAX)


def generate(file_path, velocity):
    f = open(file_path, "w")
    f.write(str(velocity))
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
    velocity = parse_args()
    file_path = '../input.txt'
    generate(file_path, velocity)
    

if __name__ == '__main__':
    main()

