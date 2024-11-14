import type { EmoticonPack } from "./emoticonPack";

export type BlacklistResult = {
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
  content: Array<BlacklistedEmoticon>;
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

export type BlacklistedEmoticon = {
  id: number;
  emoticonPack: {
    id: number;
    title: string;
    member: {
      id: number;
      email: string;
      nickname: string;
    };
    price: number;
    view: number;
    category: "REAL" | "CHARACTER" | "ENTERTAINMENT" | "LETTER";
    thumbnailImg: string;
    listImg: string;
    emoticons: string[];
    description: string;
    shareLink: string;
    createdAt: string;
    updatedAt: string;
    tags: string[];
    download: number;
    deletedAt: string;
    public: boolean;
    blacklist: boolean;
    aiGenerated: boolean;
  };
  type: "EXAMINE" | "REPORT";
  state: "PENDING" | "RECEIVED" | "APPROVED" | "REJECTED";
  createdAt: string;
  completedAt: string;
};
