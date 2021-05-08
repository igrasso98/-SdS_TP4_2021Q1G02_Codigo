import matplotlib.pyplot as plt
import numpy as np


DT_S = [0.01, 0.001, 1e-4, 1e-5, 1e-6]
DT_S_STRING = ['0.01', '0.001', '1.0E-4', '1.0E-5', '1.0E-6']


def internal_graph_system(data, label, color):
    plt.loglog(DT_S, data, marker='o', label=label.replace('"', ''), color=color)


def graph_systems(ecms, algos):
    colors = ['#1F77B4', '#FF7F0E', '#2DA02D', '#7BD7F4', '#FFB1E8', '#FBE6C9', '#07E822']
    for i, ecm in enumerate(ecms):
        internal_graph_system(ecm, algos[i], colors[0])
        colors.pop(0)

    plt.xlabel('dT log10 [s]')
    plt.ylabel('ECM log10 [m^2]')
    plt.xticks(DT_S)
    plt.legend(loc='upper right')
    plt.tight_layout()
    plt.show()
    plt.clf()


def get_value(time):
    systems = {}
    times = []
    algos = []

    with open('R:/output/oscillator_2_' + time + '.tsv', 'r') as f:
        headers = f.readline().split('\t')

        for i in range(1, len(headers)):
            algo = headers[i].strip().replace('"', '')
            algos.append(algo)
            systems[algo] = []

        line = f.readline()
        while line != '':
            data = line.split('\t')
            times.append(float(data[0].strip()))
            for i in range(1, len(data)):
                systems[algos[i - 1]].append(float(data[i].strip()))

            line = f.readline()

    ecms = []
    for i_algo in range(0, len(algos) - 1):
        ecms.append(get_ecm(systems[algos[i_algo]], systems[algos[-1]]))

    return ecms, algos


def get_values():
    ecms = []
    algos = []

    ecms_algo = None
    for dt_string in DT_S_STRING:
        ecms_per_algo, algos = get_value(dt_string)
        if ecms_algo is None:
            ecms_algo = {}
            for algo in algos:
                ecm = ecms_algo[algo] = []
                ecms.append(ecm)

        for i in range(len(ecms_per_algo)):
            ecms_algo[algos[i]].append(ecms_per_algo[i])

    return [ecm for ecm in ecms if len(ecm) > 0], algos


def get_ecm(system, real_system):
    return np.square(np.array(system) - np.array(real_system)).mean()


def main():
    ecms, algos = get_values()
    graph_systems(ecms, algos)


if __name__ == '__main__':
    main()
