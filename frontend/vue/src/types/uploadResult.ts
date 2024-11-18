export type UploadResult = {
  id: number;
  title: string;
  member: {
    id: number;
    email: string;
    nickname: string;
  };
  price: number;
  view: number;
  category: "REAL" | "CHARACTER" | "ENTERTAINMENT" | "TEXT";
  thumbnailImg: string;
  listImg: string;
  description: string;
  shareLink: string;
  createdAt: string;
  updatedAt: string;
  tags: string[];
  public: boolean;
  aiGenerated: boolean;
  blacklist: boolean;
};
