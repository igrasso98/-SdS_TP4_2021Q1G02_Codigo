import matplotlib.pyplot as plt


def internal_graph_system(v0s, factors, color):
    plt.plot(v0s, factors, marker='o', color=color)
    # for i, n in enumerate(std):
    #     plt.errorbar(time[i], std[i], marker='_', yerr=n, xuplims=True, xlolims=True, ecolor='#000000')


def graph_factors(factors, v0s):
    colors = [
        '#1F77B4',
        '#FF7F0E',
        '#2DA02D',
        '#7BD7F4',
        '#FFB1E8',
        '#FBE6C9',
        '#07E822',
        '#6A9951',
        '#E88CE2',
        '#5D2D4F',
        '#F743FC'
    ]
    internal_graph_system(v0s, factors, colors[0])

    plt.xlabel('|V0| * 10^3 [m/s]')
    plt.ylabel('Proporcion particulas que escapan')
    plt.xticks(v0s, [str(int(v0 / 1000)) for v0 in v0s])
    # plt.legend(loc='upper right')
    plt.tight_layout()
    plt.show()
    plt.clf()


def get_values():
    factors = []
    # std = []
    v0s = []

    with open('R:/output/radiation_4.tsv', 'r') as f:
        f.readline()  # Discard headers

        line = f.readline()
        while line != '':
            data = line.split('\t')
            v0s.append(float(data[0].replace('"', '').strip()))
            factors.append(float(data[2].replace('"', '').strip()) / float(data[1].replace('"', '').strip()))

            line = f.readline()

    return factors, v0s


def main():
    factors, v0s = get_values()
    graph_factors(factors, v0s)


if __name__ == '__main__':
    main()
