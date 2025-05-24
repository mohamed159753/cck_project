export interface CloudResource {
  universityName: string;
  reservations: number;
  pending: number;
  approved: number;
  TotalStorageQuotaReserved: number;
  TotalRamQuotaReserved: number;
  TotalVcpuQuotaReserved: number;
  UsedStorage: number;
  UsedRam: number;
  UsedVcpu: number;
  storageUsePercent: number;
  ramUsePercent: number;
  vcpuUsePercent: number;
  usageStatistics: {
    [month: string]: {
      vcpu: number;
      storage: number;
      ram: number;
    };
  };

}