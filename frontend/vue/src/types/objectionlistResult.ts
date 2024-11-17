import type { EmoticonPack } from "./emoticonPack";

export type ObjectionListResult = {
  totalElements: number;
  totalPages: number;
  pageable: {
    paged: boolean;
    pageNumber: number;
    pageSize: number;
    offset: number;
    sort: {
      sorted: boolean;
      empty: boolean;
      unsorted: boolean;
    };
    unpaged: boolean;
  };
  size: number;
  content: Array<Objection>;
  number: number;
  sort: {
    sorted: boolean;
    empty: boolean;
    unsorted: boolean;
  };
  numberOfElements: number;
  first: boolean;
  last: boolean;
  empty: boolean;
};

export type Objection = {
  id: number;
  emoticonPack: {
    id: number;
    title: string;
    author: {
      id: number;
      nickname: string;
      profile: string;
      bio: string;
    };
    price: number;
    view: number;
    category: "REAL" | "CHARACTER" | "ENTERTAINMENT" | "LETTER";
    thumbnailImg: string;
    listImg: string;
    emoticons: string[];
    description: string;
    createdAt: string;
    tags: string[];
    sharedLink: string;
    download: number;
    public: boolean;
    blacklist: boolean;
    aigenerated: boolean;
  };
  submitRequest: string;
  type: "EXAMINE" | "REPORT";
  state: "PENDING" | "RECEIVED" | "APPROVED" | "REJECTED";
  createdAt: string;
  completedAt: string;
};
