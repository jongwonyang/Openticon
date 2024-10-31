import { useEffect } from "react";
import React from "react";
import { useSearchParams } from "react-router-dom";

// 가맹점 식별코드를 init하는 함수
const initIMP = () => {
    const { IMP } = window;
    IMP.init("imp32011034"); // 가맹점 식별코드
};

const Home = () => {
    useEffect(() => {
        initIMP();
    }, []);

    const [searchParams] = useSearchParams();
    const accessToken = searchParams.get("access_token");
    const refreshToken = searchParams.get("refresh_token");

    const requestPay = () => {
        const { IMP } = window;
        IMP.request_pay(
            {
                pg: "html5_inicis",
                pay_method: "card",
                merchant_uid: `mid_${new Date().getTime()}`,
                name: "주문명: 결제 테스트",
                amount: 100,
                buyer_email: "buyer@example.com",
                buyer_name: "홍길동",
                buyer_tel: "010-1234-5678",
                buyer_addr: "서울특별시 강남구 삼성동",
                buyer_postcode: "123-456",
            },
            (rsp) => {
                if (rsp.success) {
                    verifyPayment(rsp.imp_uid, 1, 1, 100);
                } else {
                    alert(`결제에 실패하였습니다. 에러: ${rsp.error_msg}`);
                }
            }
        );
    };

    const verifyPayment = (imp_uid, member_id, emoticon_pack_id, point) => {
        console.log(accessToken);
        fetch(`${process.env.REACT_APP_API_BASE_URL}/api/v1/points/payment`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${accessToken}`,
            },
            body: JSON.stringify({
                imp_uid,
                emoticon_pack_id,
                point,
            }),
        })
            .then((res) => res.json())
            .then((data) => {
                if (data.status === "success") {
                    alert("결제가 완료되었습니다.");
                } else {
                    alert("결제 검증에 실패하였습니다.");
                }
            })
            .catch((error) => {
                console.error("Error:", error);
                alert("결제 검증 중 오류가 발생하였습니다.");
            });
    };

    return (
        <div id="app" style={{ textAlign: "center" }}>
            <h1>iamport 결제 테스트</h1>
            <button onClick={requestPay} style={{ padding: "10px 20px", fontSize: "16px", cursor: "pointer" }}>
                결제하기
            </button>
            <div>
                <a href={`${process.env.REACT_APP_API_BASE_URL}/api/v1/oauth2/authorization/google?redirect_uri=http://127.0.0.1:3000&mode=login`}>
                    <button>Google Login</button>
                </a>
                <a href={`${process.env.REACT_APP_API_BASE_URL}/api/v1/oauth2/authorization/google?redirect_uri=http://127.0.0.1:3000&mode=unlink`}>
                    <button>Google Unlink</button>
                </a>
                <a href={`${process.env.REACT_APP_API_BASE_URL}/api/v1/oauth2/authorization/naver?redirect_uri=http://127.0.0.1:3000&mode=login`}>
                    <button>Naver Login</button>
                </a>
                <a href={`${process.env.REACT_APP_API_BASE_URL}/api/v1/oauth2/authorization/naver?redirect_uri=http://127.0.0.1:3000&mode=unlink`}>
                    <button>Naver Unlink</button>
                </a>
                <a href={`${process.env.REACT_APP_API_BASE_URL}/api/v1/oauth2/authorization/kakao?redirect_uri=http://127.0.0.1:3000&mode=login`}>
                    <button>Kakao Login</button>
                </a>
                <a href={`${process.env.REACT_APP_API_BASE_URL}/api/v1/oauth2/authorization/kakao?redirect_uri=http://127.0.0.1:3000&mode=unlink`}>
                    <button>Kakao Unlink</button>
                </a>
                <p>Access Token : {accessToken}</p>
                <p>Refresh Token : {refreshToken}</p>
            </div>
        </div>
    );
};

// default export 추가
export default Home;
