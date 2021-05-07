import matplotlib.pyplot as plt
import numpy as np


def internal_graph_system(data, times, label, color):
    plt.plot(times, data, label=label, color=color)


def graph_systems(systems, times, algos):
    colors = ['#1F77B4', '#FF7F0E', '#2DA02D', '#7BD7F4', '#FFB1E8', '#FBE6C9', '#07E822']
    for algo in algos:
        internal_graph_system(systems[algo], times, algo, colors[0])
        colors.pop(0)

    plt.xlabel('Tiempo [s]')
    plt.ylabel('Distancia al centro [m]')
    plt.xticks([0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0])
    plt.legend(loc='upper right')
    plt.tight_layout()
    plt.show()
    plt.clf()


def get_values():
    results = {}
    times = []
    algos = []

    with open('R:/output/oscillator_1.tsv', 'r') as f:
        headers = f.readline().split('\t')

        for i in range(1, len(headers)):
            algo = headers[i].strip().replace('"', '')
            algos.append(algo)
            results[algo] = []

        line = f.readline()
        while line != '':
            data = line.split('\t')
            times.append(float(data[0].strip()))
            for i in range(1, len(data)):
                results[algos[i - 1]].append(float(data[i].strip()))

            line = f.readline()

    return results, times, algos


def get_ecm(system, real_system):
    return np.square(np.array(system) - np.array(real_system)).mean()


def main():
    systems, times, algos = get_values()
    graph_systems(systems, times, algos)

    for i in range(len(algos) - 1):
        print('ECM ' + algos[i] + ': ' + '{:e}'.format(get_ecm(systems[algos[i]], systems[algos[-1]])))


if __name__ == '__main__':
    main()
