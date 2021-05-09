import matplotlib.pyplot as plt


def internal_graph_system(time, mean, std, label, color):
    plt.plot(time, mean, label=label + '*10^3 m/s', color=color)
    # for i, n in enumerate(std):
    #     plt.errorbar(time[i], std[i], marker='_', yerr=n, xuplims=True, xlolims=True, ecolor='#000000')


def graph_systems(systems, v0s):
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
    for i, system in enumerate(systems):
        internal_graph_system(system['time'], system['mean'], system['std'], v0s[i], colors[0])
        colors.pop(0)

    plt.xlabel('Tiempo [s]')
    plt.ylabel('Distancia recorrida [m]')
    # plt.xticks(max_time)
    plt.legend(loc='upper right')
    plt.tight_layout()
    plt.show()
    plt.clf()


def get_values():
    systems = []
    v0s = []

    with open('R:/output/radiation_3.tsv', 'r') as f:
        headers = f.readline().split('\t')

        for i in range(1, len(headers), 2):
            v0s.append('{:.0f}'.format(float(headers[i].replace('"', '').strip()) / 1000))
            systems.append({
                'time': [],
                'mean': [],
                'std': []
            })

        line = f.readline()
        while line != '':
            data = line.split('\t')
            for i in range(1, len(data), 2):
                word = data[i].strip()
                if word != 'null':
                    systems[int((i - 1) / 2)]['time'].append(float(data[0].strip()))
                    systems[int((i - 1) / 2)]['mean'].append(float(data[i].strip()))
                    systems[int((i - 1) / 2)]['std'].append(float(data[i + 1].strip()))

            line = f.readline()

    return systems, v0s


def main():
    systems, v0s = get_values()
    graph_systems(systems, v0s)


if __name__ == '__main__':
    main()
