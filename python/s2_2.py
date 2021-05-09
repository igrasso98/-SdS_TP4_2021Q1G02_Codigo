import matplotlib.pyplot as plt


DT_S = [1.0E-14, 1.0E-15, 1.0E-16, 1.0E-17, 1.0E-18]
DT_S_STRING = ['1.0E-14', '1.0E-15', '1.0E-16', '1.0E-17', '1.0E-18']


def internal_graph_system(time, data, label, color):
    plt.semilogy(time, data, label=label.replace('"', ''), color=color)


def graph_systems(systems):
    colors = ['#1F77B4', '#FF7F0E', '#2DA02D', '#7BD7F4', '#FFB1E8', '#FBE6C9', '#07E822']
    for i, system in enumerate(systems):
        internal_graph_system(system['time'], system['data'], DT_S_STRING[i], colors[0])
        colors.pop(0)

    plt.xlabel('Tiempo [s]')
    plt.ylabel('log10 |E(t=0) - E(t>0)| [J]')
    # plt.xticks(max_time)
    plt.legend(loc='upper right')
    plt.tight_layout()
    plt.show()
    plt.clf()


def get_values():
    systems = []

    with open('R:/output/radiation_2.tsv', 'r') as f:
        headers = f.readline().split('\t')

        for i in range(1, len(headers)):
            systems.append({
                'time': [],
                'data': []
            })

        line = f.readline()
        while line != '':
            data = line.split('\t')
            for i in range(1, len(data)):
                word = data[i].strip()
                if word != 'null':
                    systems[i - 1]['time'].append(float(data[0].strip()))
                    systems[i - 1]['data'].append(float(data[i].strip()))

            line = f.readline()

    return systems


def main():
    graph_systems(get_values())


if __name__ == '__main__':
    main()
