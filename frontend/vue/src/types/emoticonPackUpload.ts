export type EmoticonPackUploadInfo = {
  packTitle: string;
  isAiGenerated: boolean;
  isPublic: boolean;
  category: "REAL" | "CHARACTER" | "ENTERTAINMENT" | "TEXT"; // 카테고리 타입은 실제 API에 맞게 조정하세요
  description: string;
  price: number;
  tags: string[];
}

export type EmoticonPackUploadFiles = {
  thumbnailImg: File | null;
  listImg: File | null;
  emoticons: File[];
} 